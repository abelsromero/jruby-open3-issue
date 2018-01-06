require 'open3'
require 'shellwords'
require 'rbconfig'

def is_java?
    RUBY_PLATFORM.downcase == "java"
end

def is_mac?
    RbConfig::CONFIG['target_os'].downcase.include?("darwin")
end

def is_windows?
    RbConfig::CONFIG['target_os'].downcase.include?("mswin")
end

if ENV['KINDLEGEN']
    puts ENV['KINDLEGEN']
else 
    puts "KINDLEGEN env_var was not set"
    # JRUBY
    if is_java?
        if is_mac?
            ENV['KINDLEGEN'] = "#{Dir.pwd}/files/kindlegen"        
        elsif is_windows?
            ENV['KINDLEGEN'] = "#{Dir.pwd}/files/kindlegen.exe"                    
        else
            puts "Linux not supported (yet)"
            return
        end
    # Native Ruby
    elsif is_mac?
        ENV['KINDLEGEN'] = "#{Dir.pwd}/kindlegen"
    elsif is_windows?
        ENV['KINDLEGEN'] = "#{Dir.pwd}/kindlegen.exe"
    else
        puts "Linux not supported (yet)"
        return
    end
    puts "Set to: #{ENV['KINDLEGEN']}"    
end

kindlegen_cmd = ENV['KINDLEGEN']
puts "executable? kindlegen: #{::File.executable? kindlegen_cmd}"

mobi_file = 'spine.mobi'
compress_flag = '-c0'
epub_file = ::File.absolute_path 'spine-kf8.epub'

# Running from JRuby
unless ::File.file? epub_file
    epub_file = ::File.absolute_path "files/spine-kf8.epub"
end
 
puts epub_file
puts "file? epub_file: #{::File.file? epub_file}"

cmd = [kindlegen_cmd, '-dont_append_source', compress_flag, '-o', mobi_file, epub_file].compact
::Open3.popen2e(::Shellwords.join cmd) {|input, output, wait_thr|
    output.each {|line| puts line } unless $VERBOSE.nil?
}


puts "END"
