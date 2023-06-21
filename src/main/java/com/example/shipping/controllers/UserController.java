package com.example.shipping.controllers;

import com.example.shipping.models.ERole;
import com.example.shipping.models.Role;
import com.example.shipping.models.User;
import com.example.shipping.payload.request.LoginRequest;
import com.example.shipping.payload.request.SignupRequest;
import com.example.shipping.payload.request.UserRequest;
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

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {
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

	/**
	 * 用户查询
	 * @param userName
	 * @param realName
	 * @return
	 */
	@GetMapping("/userList")
	public Result queryUserList(@RequestParam(required = false,name = "userName") String userName,
								@RequestParam(required = false,name = "realName") String realName){
		Result res = new Result();
		Map<String, Object> map = new HashMap<>();
		try{
			Set<Role> roleSet=new HashSet<>();
			Role shipperRole = roleRepository.findByRoleName(ERole.ROLE_SHIPPER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roleSet.add(shipperRole);
				List<User> list = userRepository.findByUsernameContainingAndRealNameContaining(userName,realName);
				List<User> userList = new ArrayList<>();
					// 去除重复查询
				list = list.stream().distinct().collect(Collectors.toList());
				for(User u : list) {
					if(u.getRoles().contains(shipperRole)) {
						userList.add(u);
					}
				}
				map.put("userList",userList);
				res.setMessage("返回成功");
				res.setData(map);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(resultCode.ERROR);
			res.setMessage(resultCode.getMsg(resultCode.ERROR));
		}
		return res;
	}

	@GetMapping("/carrierList")
	public Result queryCarrierList(@RequestParam(required = false,name = "userName") String userName,
								@RequestParam(required = false,name = "realName") String realName){
		Result res = new Result();
		Map<String, Object> map = new HashMap<>();
		try{
			Set<Role> roleSet=new HashSet<>();
			Role carrierRole = roleRepository.findByRoleName(ERole.ROLE_CARRIER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roleSet.add(carrierRole);
			List<User> list = userRepository.findByUsernameContainingAndRealNameContaining(userName,realName);
			List<User> l = new ArrayList<>();
			// 去除重复查询
			list = list.stream().distinct().collect(Collectors.toList());
			for(User u: list){
				if(u.getRoles().contains(carrierRole)) {
					l.add(u);
				}
			}
			map.put("carrierList",l);
			res.setMessage("返回成功");
			res.setData(map);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(resultCode.ERROR);
			res.setMessage(resultCode.getMsg(resultCode.ERROR));
		}
		return res;
	}

	/**
	 * 修改用戶
	 * @param userRequest
	 * @return
	 */
	@PostMapping("/modifyUser")
	public Result modifyUser(@Valid @RequestBody UserRequest userRequest) {
		Result res = new Result();
		Map<String, Object> response = new HashMap<>();
		if (!userRepository.existsById(userRequest.getId())) {
			return ResponseEntity
					.badRequest()
					.body(new Result(ResultCode.EXISTERR, "Error: user not found!", response)).getBody();
		}

		User user = new User(userRequest.getUsername(),
				userRequest.getRealName(),
				userRequest.getPhone(),
				encoder.encode(userRequest.getPassword()));

		System.out.println(user);
		Long id = userRequest.getId();
		user.setId(id);
		Set<Role> roles = new HashSet<>();
		Role role = roleRepository.findByRoleName(ERole.ROLE_SHIPPER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(role);
		user.setRoles(roles);

		if(userRepository.existsByUsername(user.getUsername())){
			if(userRepository.findByUsername(user.getUsername()).get().getId() != id) {
				res.setCode(ResultCode.InfoModERR);
				res.setMessage("用户名已存在");
				return res;
			}
		}

		if(userRepository.existsByPhone(user.getPhone())){
			if(userRepository.findByPhone(user.getPhone()).getId() != id) {
				res.setCode(ResultCode.InfoModERR);
				res.setMessage("联系方式已存在");
				return res;
			}
		}

		//JPA 新增和修改用的都是save方法. 它根据实体类的id是否为0来判断是进行增加还是修改.
		try{
			userRepository.save(user);
			res.setMessage("用户信息更新成功");
		}catch (Exception e){
			res.setCode(ResultCode.InfoModERR);
			res.setMessage(resultCode.getMsg(ResultCode.InfoModERR));
		}

		return res;
	}

	/**
	 * 修改員工
	 * @param userRequest
	 * @return
	 */
	@PostMapping("/modifyCarrier")
	public Result modifyCarrier(@Valid @RequestBody UserRequest userRequest) {
		Result res = new Result();
		Map<String, Object> response = new HashMap<>();
		if (!userRepository.existsById(userRequest.getId())) {
			return ResponseEntity
					.badRequest()
					.body(new Result(ResultCode.EXISTERR, "Error: user not found!", response)).getBody();
		}

		LocalDate a = LocalDate.parse(userRequest.getEntryTime());
		LocalDate b = LocalDate.parse(userRequest.getBirthday());
		User user = new User(userRequest.getUsername(),
				userRequest.getRealName(),
				userRequest.getPhone(),
				userRequest.getSex(),
				a,
				b,
				encoder.encode(userRequest.getPassword()));

		System.out.println(user);
		Long id = userRequest.getId();
		user.setId(id);
		Set<Role> roles = new HashSet<>();
		Role role = roleRepository.findByRoleName(ERole.ROLE_CARRIER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(role);
		user.setRoles(roles);

		if(userRepository.existsByUsername(user.getUsername())){
			if(userRepository.findByUsername(user.getUsername()).get().getId() != id) {
				res.setCode(ResultCode.InfoModERR);
				res.setMessage("用户名已存在");
				return res;
			}
		}

		System.out.println(user.getPhone());
		if(userRepository.existsByPhone(user.getPhone())){
			if(userRepository.findByPhone(user.getPhone()).getId() != id) {
				res.setCode(ResultCode.InfoModERR);
				res.setMessage("联系方式已存在");
				return res;
			}
		}

		//JPA 新增和修改用的都是save方法. 它根据实体类的id是否为0来判断是进行增加还是修改.
		try{
			userRepository.save(user);
			res.setMessage("員工信息更新成功");
		}catch (Exception e){
			res.setCode(ResultCode.InfoModERR);
			res.setMessage(resultCode.getMsg(ResultCode.InfoModERR));
		}

		return res;
	}


	/**
	 * 批量删除
	 * @param params
	 * @return
	 */
	@DeleteMapping("/deleteUsers")
	public Result deleteUser(@RequestBody Map<String, Object> params){
		Result res = new Result();
		List<User> users = new ArrayList<>();
		List<Integer> ids = (List<Integer>) params.get("ids");
		for(int i:ids) {
			User u = userRepository.findById(Long.valueOf(i)).get();
			users.add(u);
		}
		try {
			userRepository.deleteInBatch(users);
			res.setMessage("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResultCode.ERROR);
			res.setMessage(resultCode.getMsg(ResultCode.ERROR));
		}
		return res;
	}

	/**
	 * 增加用戶
	 * @param user
	 * @return
	 */
	@PostMapping("/addUser")
	public Result addUser(@RequestBody User user){
		Result res = new Result();
		Set<Role> rr=new HashSet<>();
		Role role = roleRepository.findByRoleName(ERole.ROLE_SHIPPER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		rr.add(role);
		user.setRoles(rr);
		user.setPassword(encoder.encode(user.getPassword()));
		if(userRepository.existsByUsername(user.getUsername())) {
			res.setCode(ResultCode.EXISTERR);
			res.setMessage("该用户名已存在");
			return res;
		}
		if(userRepository.existsByPhone(user.getPhone())) {
			res.setCode(ResultCode.EXISTERR);
			res.setMessage("该电话号码已存在");
			return res;
		}
		try {
			User r = userRepository.save(user);
			if(r.getId()!=null){
				res.setMessage("新增用户成功");
			}else{
				res.setCode(ResultCode.EXISTERR);
				res.setMessage(resultCode.getMsg(ResultCode.EXISTERR));
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResultCode.ERROR);
			res.setMessage(resultCode.getMsg(ResultCode.ERROR));
		}

		return res;
	}

	/**
	 * 增加员工
	 * @param user
	 * @return
	 */
	@PostMapping("/addCarrier")
	public Result addCarrier(@RequestBody User user){
		Result res = new Result();
		Set<Role> rr=new HashSet<>();
		Role role = roleRepository.findByRoleName(ERole.ROLE_CARRIER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		rr.add(role);
		user.setRoles(rr);
		user.setPassword(encoder.encode(user.getPassword()));
		if(userRepository.existsByUsername(user.getUsername())) {
			res.setCode(ResultCode.EXISTERR);
			res.setMessage("该用户名已存在");
			return res;
		}
		if(userRepository.existsByPhone(user.getPhone())) {
			res.setCode(ResultCode.EXISTERR);
			res.setMessage("该电话号码已存在");
			return res;
		}
		try {
			User r = userRepository.save(user);
			if(r.getId()!=null){
				res.setMessage("新增员工成功");
			}else{
				res.setCode(ResultCode.EXISTERR);
				res.setMessage(resultCode.getMsg(ResultCode.EXISTERR));
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResultCode.ERROR);
			res.setMessage(resultCode.getMsg(ResultCode.ERROR));
		}

		return res;
	}
}
