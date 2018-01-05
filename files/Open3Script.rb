require 'open3'
require 'shellwords'

if ENV['KINDLEGEN']
    puts ENV['KINDLEGEN']
else 
    puts "KINDLEGEN env_var is not set"
end

kindlegen_cmd = ENV['KINDLEGEN']
puts ::File.executable? kindlegen_cmd

mobi_file = 'spine.mobi'
compress_flag = '-c0'
epub_file = ::File.absolute_path 'spine-kf8.epub'

# Running from JRuby
unless ::File.file? epub_file
    epub_file = ::File.absolute_path "files/spine-kf8.epub"
end
 
puts epub_file
puts ::File.file? epub_file

cmd = [kindlegen_cmd, '-dont_append_source', compress_flag, '-o', mobi_file, epub_file].compact
::Open3.popen2e(::Shellwords.join cmd) {|input, output, wait_thr|
    output.each {|line| puts line } unless $VERBOSE.nil?
}

puts "END"
