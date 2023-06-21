package com.example.shipping.security.utils;

public class ResultCode {

    public static Integer SUCCESS = 200; //成功
    public static Integer ERROR = 100; //服务器错误
    public static Integer LOGINERR = 101; //用户名或密码错误
    public static Integer INFOERR = 102; //用户信息不匹配
    public static Integer EXISTERR = 103; //信息已存在
    public static Integer InfoGetERR = 104; //信息获取失败
    public static Integer InfoModERR = 105; //信息增加失败
    public static Integer InfoDelERR = 106; //信息删除失败
    public static Integer HouseERR = 107; //信息修改失败
    public static Integer REGERR=109;//文件为空
    public static Integer bERR =110;//文件上传失败
    public static Integer cERR =111;//重复操作

    public String getMsg(Integer code){
        String res = "";
        if(code.equals(SUCCESS)){
            res = "成功";
        }else if(code.equals(ERROR)){
            res = "服务器错误";
        }else if(code.equals(LOGINERR)){
            res = "用户名或密码错误";
        }else if(code.equals(INFOERR)){
            res = "用户信息不匹配";
        }else if(code.equals(EXISTERR)){
            res = "信息已存在";
        }else if(code.equals(InfoGetERR)){
            res = "信息获取错误";
        }else if(code.equals(InfoDelERR)){
            res = "信息删除错误";
        }else if(code.equals(InfoModERR)){
            res = "信息修改错误";
        }else if(code.equals(HouseERR)){
            res = "需要先将仓库内货物转移才能删除";
        }else if(code.equals(REGERR)){
            res = "需要先将仓库内货物转移才能删除";
        }
        return res;
    }

}




