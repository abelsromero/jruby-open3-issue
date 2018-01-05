package jruby.demo;

import jruby.demo.internal.EnvironmentInjector;
import org.jruby.CompatVersion;
import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.RubyRuntimeAdapter;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.io.FileUtils.readFileToString;

public class RubyRunner {

    public static final String SCRIPT_NAME = "files/Open3Script.rb";

    public static void main(String[] args) throws IOException {

        final File projectPath = new File(".").getAbsoluteFile();

        final RubyInstanceConfig config = createRubyConfig(projectPath);
        final Ruby runtime = createRubyRuntime(config);
        final RubyRuntimeAdapter evaler = JavaEmbedUtils.newRuntimeAdapter();

        final String script = readFileToString(new File(projectPath, SCRIPT_NAME), StandardCharsets.UTF_8);

        assert script.length() > 0;

        IRubyObject result = evaler.eval(runtime, script);
        System.out.println(result);
    }

    private static Ruby createRubyRuntime(RubyInstanceConfig config) {
        final List<String> loadPaths = new ArrayList<>();
        return JavaEmbedUtils.initialize(loadPaths, config);
    }

    private static RubyInstanceConfig createRubyConfig(File basePath) {

        File execFile = new File(basePath, "files/kindlegen.exe");
        assert execFile.exists();

        final Map<String, String> envVars = new HashMap<String, String>() {{
            put("KINDLEGEN", execFile.getAbsolutePath());
        }};

        final RubyInstanceConfig config = createOptimizedConfiguration();
        injectEnvironmentVariables(config, envVars);
        return config;
    }

    // Taken from Asciidoctorj
    private static RubyInstanceConfig createOptimizedConfiguration() {
        RubyInstanceConfig config = new RubyInstanceConfig();
        config.setCompatVersion(CompatVersion.RUBY2_0);
        config.setCompileMode(RubyInstanceConfig.CompileMode.OFF);
        return config;
    }

    // Taken from Asciidoctorj
    private static void injectEnvironmentVariables(RubyInstanceConfig config, Map<String, String> environmentVars) {
        EnvironmentInjector environmentInjector = new EnvironmentInjector(config);
        environmentInjector.inject(environmentVars);
    }

}
