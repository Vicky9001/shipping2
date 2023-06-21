package com.example.shipping.controllers;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.shipping.models.ERole;
import com.example.shipping.models.Role;
import com.example.shipping.payload.request.LoginRequest;
import com.example.shipping.payload.request.SignupRequest;
import com.example.shipping.repository.RoleRepository;
import com.example.shipping.repository.UserRepository;
import com.example.shipping.security.jwt.JwtUtils;
import com.example.shipping.security.services.UserDetailsImpl;
import com.example.shipping.security.utils.Result;
import com.example.shipping.security.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.shipping.models.User;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	ResultCode resultCode;

	@PostMapping("/login")
	public Result authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Map<String, Object> response = new HashMap<>();
		try {
				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

				//SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = jwtUtils.generateJwtToken(authentication);

				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				List<String> roles = userDetails.getAuthorities().stream()
						.map(item -> item.getAuthority())
						.collect(Collectors.toList());

			response.put("name", userDetails.getUsername());
			response.put("phone", userDetails.getPhone());
			response.put("id", userDetails.getId());
			response.put("roles",roles);//roleId roleName
			response.put("token", jwt);

			return ResponseEntity.ok(new Result(ResultCode.SUCCESS, "Success", response)).getBody();
//				return ResponseEntity.ok(new JwtResponse(jwt,
//														 userDetails.getId(),
//														 userDetails.getUsername(),
//														 userDetails.getEmail(),
//														 roles));

		} catch (BadCredentialsException ex) {
			return ResponseEntity.badRequest()
					.body(new Result(ResultCode.INFOERR, "Fail", response)).getBody();
		}
    }

	@PostMapping("/signup")
	public Result registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		Map<String, Object> response = new HashMap<>();
		if (userRepository.existsByUsername(signUpRequest.getUserName())) {
			return ResponseEntity
					.badRequest()
					.body(new Result(ResultCode.EXISTERR, "Error: Username is already taken!", response)).getBody();
		}

		if (userRepository.existsByPhone(signUpRequest.getPhone())) {
			return ResponseEntity
					.badRequest()
					.body(new Result(ResultCode.EXISTERR, "Error: Phone is already in use!", response)).getBody();
		}

		// Create new user's account
		User user = new User(signUpRequest.getUserName(),
							 signUpRequest.getPhone(),
							 encoder.encode(signUpRequest.getPassword()));

		user.setRealName(signUpRequest.getRealName());

		Set<Integer> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if(!strRoles.contains(0)) {
			if (signUpRequest.getSex()>=0) {
				user.setSex(signUpRequest.getSex());
			} else {
				return ResponseEntity
						.badRequest()
						.body(new Result(ResultCode.EXISTERR, "Error: Sex not found!", response)).getBody();
			}
			System.out.println(signUpRequest.getEntryTime());
			Optional<String> optionalEntryTime = Optional.ofNullable(signUpRequest.getEntryTime());
			if (optionalEntryTime.isPresent()) {
				System.out.println(signUpRequest.getEntryTime());
				user.setEntryTime(LocalDate.parse(signUpRequest.getEntryTime()));
			} else {
				return ResponseEntity
						.badRequest()
						.body(new Result(ResultCode.EXISTERR, "Error: EntryTime not found!", response)).getBody();
			}

			Optional<String> optionalBirthday = Optional.ofNullable(signUpRequest.getBirthday());
			if (optionalBirthday.isPresent()) {
				user.setBirthday(LocalDate.parse(signUpRequest.getBirthday()));
			} else {
				return ResponseEntity
						.badRequest()
						.body(new Result(ResultCode.EXISTERR, "Error: Birthday not found!", response)).getBody();
			}
		}

		if (strRoles == null) {
			Role userRole = roleRepository.findByRoleName(ERole.ROLE_SHIPPER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case 0:
					Role shipperRole = roleRepository.findByRoleName(ERole.ROLE_SHIPPER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(shipperRole);

					break;
				case 1:
					Role carrierRole = roleRepository.findByRoleName(ERole.ROLE_CARRIER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(carrierRole);

					break;
				case 2:
					Role managerRole = roleRepository.findByRoleName(ERole.ROLE_MANAGER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(managerRole);

					break;
				default:
					throw new RuntimeException("Error: Role is not found.");
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		response.put("name", user.getUsername());
		response.put("phone", user.getPhone());
		List<Map<String, Object>> roleList = new ArrayList<>();
		for (Role role : roles) {
			Map<String, Object> roleMap = new HashMap<>();
			roleMap.put("role", role.getRoleName());
			// 其他属性
			roleList.add(roleMap);
		}
		response.put("roles",roleList);

		return ResponseEntity.ok(new Result(ResultCode.SUCCESS, "Success", response)).getBody();
	}

	@GetMapping("/info")  // "token:xxx"
	public Result info(String token){
		Result res = new Result();
		Map<String, Object> map = new HashMap<>();

		try {
			String username = jwtUtils.getUserNameFromJwtToken(token);
			User user = userRepository.findByUsername(username).get();
			if(user!=null){
				String url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.gmz88.com%2Fuploadimg%2Fimage%2F20190116%2F15476240655c3ede81c64116.77854307.jpeg&refer=http%3A%2F%2Fimg.gmz88.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1667038105&t=0374e9cdf9ecb5833b85cbe9bae9a4c8";
				map.put("userId",user.getId());
				map.put("userName",username);
				map.put("realName",user.getRealName());
				map.put("phone",user.getPhone());
				map.put("password",user.getPassword());
//				Set<Role> r=user.getRoles();
//				Iterator<Role> iterator = r.iterator();
//				Role rr = iterator.next();
//				Map<String, Object> roles=new HashMap<>();
//				roles.put("roleId",rr.getRoleId());
//				roles.put("roleName",rr.getRoleName());
//				map.put("roles",roles);
				map.put("roles",user.getRoles());
				map.put("avatar",url);
				res.setMessage("返回成功");
				res.setData(map);
			}else {
				res.setCode(ResultCode.INFOERR);
				res.setMessage(resultCode.getMsg(ResultCode.INFOERR));
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResultCode.ERROR);
			res.setMessage(resultCode.getMsg(ResultCode.ERROR));
		}
		return res;
	}

	@PostMapping("/logout")  // "token:xxx"
	public Result logout(){
		Result res = new Result();
		res.setMessage("登出成功");
		return res;
	}
}
