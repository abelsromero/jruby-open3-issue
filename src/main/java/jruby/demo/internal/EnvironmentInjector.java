package jruby.demo.internal;

import org.jruby.RubyInstanceConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

// Taken from Asciidoctorj
public class EnvironmentInjector {

    private RubyInstanceConfig config;

    public EnvironmentInjector(RubyInstanceConfig config) {
        this.config = config;
    }

    public void inject(Map<String, String> environmentVars) {
        // if (!environmentVars.isEmpty()) {
            Map<String, String> replacementEnv = new HashMap<>(System.getenv());
            for (Map.Entry<String, String> envVar : environmentVars.entrySet()) {
                String key = envVar.getKey();
                Object val = envVar.getValue();
                if (val == null || "".equals(val)) {
                    replacementEnv.remove(envVar.getKey());
                    if ("GEM_PATH".equals(key) && !environmentVars.containsKey("GEM_HOME")) {
                        replacementEnv.remove("GEM_HOME");
                    }
                } else {
                    replacementEnv.put(key, (String) val);
                    if ("GEM_PATH".equals(key) && !environmentVars.containsKey("GEM_HOME")) {
                        String gemHome = val.toString().split(File.pathSeparator)[0];
                        replacementEnv.put("GEM_HOME", gemHome);
                    }

                }

            }
            config.setEnvironment(replacementEnv);
       // }
    }

}
