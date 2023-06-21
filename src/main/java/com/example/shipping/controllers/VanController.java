package com.example.shipping.controllers;

import com.example.shipping.models.Van;
import com.example.shipping.repository.VanRepository;
import com.example.shipping.security.utils.Result;
import com.example.shipping.security.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/van")
public class VanController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	VanRepository vanRepository;

	ResultCode resultCode;

	/**
	 * 查询
	 * @param license
	 * @param state
	 * @return
	 */
	@GetMapping("/vanList")
	public Result queryVanList(@RequestParam(required = false,name = "license") String license,
								@RequestParam(required = false,name = "state") String state){
		Result res = new Result();
		Map<String, Object> map = new HashMap<>();
		try{
			List<Van> list;
			if(state.isEmpty()) {
				list = vanRepository.findByLicenseContaining(license);
			} else {
				int a = Integer.parseInt(state);
				list = vanRepository.findByLicenseContainingAndState(license, a);
			}
				// 去除重复查询
			list = list.stream().distinct().collect(Collectors.toList());
			map.put("vanList",list);
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
	 * @param
	 * @return
	 */
	@PostMapping("/modifyVan")
	public Result modifyUser(@Valid @RequestBody Van van) {
		Result res = new Result();
		Map<String, Object> response = new HashMap<>();
		if (!vanRepository.existsById(van.getId())) {
			res.setCode(ResultCode.InfoModERR);
			res.setMessage("此纪录不存在");
			return res;
		}

		if(vanRepository.existsByLicense(van.getLicense())){
			if (vanRepository.findByLicense(van.getLicense()).getId() != van.getId()) {
				res.setCode(ResultCode.InfoModERR);
				res.setMessage("车牌号已存在");
				return res;
			}
		}

		//JPA 新增和修改用的都是save方法. 它根据实体类的id是否为0来判断是进行增加还是修改.
		try{
			vanRepository.save(van);
			res.setMessage("车辆信息更新成功");
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
	@DeleteMapping("/deleteVans")
	public Result deleteVans(@RequestBody Map<String, Object> params){
		Result res = new Result();
		List<Van> vans = new ArrayList<>();
		List<Integer> ids = (List<Integer>) params.get("ids");
		for(int i:ids) {
			Van v = vanRepository.findById(Long.valueOf(i)).get();
			vans.add(v);
		}
		try {
			vanRepository.deleteInBatch(vans);
			res.setMessage("删除车辆成功");
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResultCode.ERROR);
			res.setMessage(resultCode.getMsg(ResultCode.ERROR));
		}
		return res;
	}

    /**
     * 增加车辆
	 * @param van
     * @return
	 */
	@PostMapping("/addVan")
	public Result addUser(@RequestBody Van van){
		Result res = new Result();
		if(vanRepository.existsByLicense(van.getLicense())) {
			res.setCode(ResultCode.EXISTERR);
			res.setMessage("该牌照已存在");
			return res;
		}
		try {
			Van r = vanRepository.save(van);
			if(r.getId()!=null){
				res.setMessage("新增车辆成功");
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
