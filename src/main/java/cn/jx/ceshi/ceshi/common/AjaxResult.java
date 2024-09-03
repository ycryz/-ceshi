package cn.jx.ceshi.ceshi.common;

import java.util.HashMap;

/**
* @ClassName: AjaxResult
* @Description: TODO(ajax操作消息提醒)
* @author fuce
* @date 2018年8月18日
*
 */
public class AjaxResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;
 
    /**
     * 初始化一个新创建的 Message 对象
     */
    public AjaxResult()
    {
    }
 
    /**
     * 返回错误消息
     * 
     * @return 错误消息
     */
    public static AjaxResult error()
    {
        return error(1, "操作失败");
    }
 
    /**
     * 返回错误消息
     * 
     * @param msg 内容
     * @return 错误消息
     */
    public static AjaxResult error(String msg)
    {
        return error(500, msg);
    }
 
    /**
     * 返回错误消息
     * 
     * @param code 错误码
     * @param msg 内容
     * @return 错误消息
     */
    public static AjaxResult error(int code, String msg)
    {
        AjaxResult json = new AjaxResult();
        json.put("code", code);
        json.put("msg", msg);
        return json;
    }
 
    /**
     * 返回成功消息
     * 
     * @param data 内容
     * @return 成功消息
     */
    public static AjaxResult success(Object data)
    {
        AjaxResult json = new AjaxResult();
        json.put("code", 200);
        json.put("msg", "请求成功");
        json.put("data",data);
        return json;
    }
    
    /**
     * 返回成功消息
     * 
     * @return 成功消息
     */
    public static AjaxResult success()
    {
        return AjaxResult.success("操作成功");
    }
    
    public static AjaxResult successData(int code, Object value){
    	 AjaxResult json = new AjaxResult();
    	 json.put("code", code);
         json.put("data", value);
         return json;
    }
 
    /**
     * 返回新增立项申报后的信息
     * @param code
     * @param msg
     * @param id
     * @return
     */
    public static AjaxResult successProjectData(int code, String msg, String id,String pId){
        AjaxResult json = new AjaxResult();
        json.put("code", code);
        json.put("msg", msg);
        json.put("id", id);
        json.put("pId",pId);
        return json;
    }
 
 
 
    /**
     * 返回项目基本信息
     * @param code
     * @param msg
     * @param data
     * @return
     */
    public static AjaxResult successProjectInfoData(int code, String msg, Object data){
        AjaxResult json = new AjaxResult();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", data);
        return json;
    }
    
    /**
     * 返回成功消息
     * 
     * @param key 键值
     * @param value 内容
     * @return 成功消息
     */
    @Override
    public AjaxResult put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }
}