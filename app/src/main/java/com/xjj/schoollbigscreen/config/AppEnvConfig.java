package com.xjj.schoollbigscreen.config;

public enum AppEnvConfig {
    /**
     * 测试环境
     */
    TEST(1,
            "测试环境",
            "http://test.imzhiliao.com/",
            "https://testsaas.imeduplus.com/eduplus-safe-campus/"),

    RELEASE(2,
            "正式环境",
            "http://imzhiliao.com:9000/",
            "https://web.imeduplus.com/eduplus-safe-campus/");

    private int index;
    private String name;
    private String apiUrl;
    private String apiUrl2;

    AppEnvConfig(int index, String name, String apiUrl, String apiUrl2) {
        this.index = index;
        this.name = name;
        this.apiUrl = apiUrl;
        this.apiUrl2 = apiUrl2;
    }

    public int getIndex() {
        return index;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiUrl2() {
        return apiUrl2;
    }

    public String getName() {
        return name;
    }

    public static AppEnvConfig typeOf(String appEnvName) {
        return valueOf(appEnvName);
    }
}
