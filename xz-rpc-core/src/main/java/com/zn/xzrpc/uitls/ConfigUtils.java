package com.zn.xzrpc.uitls;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.zn.xzrpc.config.RpcConfig;

/**
 * 配置工具类
 */
public class ConfigUtils {

    /**
     * 配置文件名
     */
    private static final String CONFIG_FILE_NAME = "application";

    /**
     * 加载配置
     * @param tClass
     * @param prefix
     * @return
     * @param <T>
     */
    public static<T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 加载配置（区分环境）
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    public static<T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configBuilder = new StringBuilder(CONFIG_FILE_NAME);
        if (StrUtil.isNotBlank(environment)) {
            configBuilder.append("-").append(environment);
        }
        configBuilder.append(".properties");
        String configFileName = configBuilder.toString();
        Props prop = Props.getProp(configFileName);
        return prop.toBean(tClass, prefix);
    }

}
