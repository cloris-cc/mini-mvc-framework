package cn.teamwang.framework.bean;

/**
 * @author Jacky
 * Date: 2020/12/11 18:24
 */
public class ResponseData {

    private final Object model;

    public ResponseData(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
