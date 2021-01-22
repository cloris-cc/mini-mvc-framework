package cn.teamwang.framework.bean;

import cn.teamwang.framework.util.CastUtil;

import java.util.Map;

/**
 * 请求参数
 *
 * @author Jacky
 * Date: 2020/12/11 18:04
 */
public class RequestParam {

    private Map<String, Object> param;

    public RequestParam(Map<String, Object> param) {
        this.param = param;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    /**
     * 获取 long 类型的参数值
     */
    public long getLong(String key) {
        return CastUtil.castLong(param.get(key));
    }
}
