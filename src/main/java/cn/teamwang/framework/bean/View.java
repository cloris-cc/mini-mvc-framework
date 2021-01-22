package cn.teamwang.framework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图对象
 *
 * @author Jacky
 * Date: 2020/12/11 18:13
 */
public class View {

    private final String path;
    private final Map<String, Object> model;

    public View(String path) {
        this.path = path;
        this.model = new HashMap<>();
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public View addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }

}
