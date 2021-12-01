package com.bsg.dbscale.cm.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmImageTemplate implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "image")
    private CmImageBase image;

    @JSONField(name = "template")
    private Template template;

    public CmImageBase getImage() {
        return image;
    }

    public void setImage(CmImageBase image) {
        this.image = image;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return "CmImageTemplate [image=" + image + ", template=" + template + "]";
    }

    public class Template {
        @JSONField(name = "config_file")
        private String configFile;

        @JSONField(name = "keysets")
        private List<Keyset> keysets;

        public String getConfigFile() {
            return configFile;
        }

        public void setConfigFile(String configFile) {
            this.configFile = configFile;
        }

        public List<Keyset> getKeysets() {
            return keysets;
        }

        public void setKeysets(List<Keyset> keysets) {
            this.keysets = keysets;
        }

        @Override
        public String toString() {
            return "CmImageTemplate [configFile=" + configFile + ", keysets=" + keysets + "]";
        }

        public class Keyset {
            @JSONField(name = "can_set")
            private Boolean canSet;

            @JSONField(name = "default")
            private String defaultValue;

            @JSONField(name = "desc")
            private String desc;

            @JSONField(name = "key")
            private String key;

            @JSONField(name = "must_restart")
            private Boolean mustRestart;

            @JSONField(name = "range")
            private String range;

            @JSONField(name = "value")
            private String value;

            public Boolean getCanSet() {
                return canSet;
            }

            public void setCanSet(Boolean canSet) {
                this.canSet = canSet;
            }

            public String getDefaultValue() {
                return defaultValue;
            }

            public void setDefaultValue(String defaultValue) {
                this.defaultValue = defaultValue;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public Boolean getMustRestart() {
                return mustRestart;
            }

            public void setMustRestart(Boolean mustRestart) {
                this.mustRestart = mustRestart;
            }

            public String getRange() {
                return range;
            }

            public void setRange(String range) {
                this.range = range;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "Keyset [canSet=" + canSet + ", defaultValue=" + defaultValue + ", desc=" + desc + ", key=" + key
                        + ", mustRestart=" + mustRestart + ", range=" + range + ", value=" + value + "]";
            }

        }
    }

}
