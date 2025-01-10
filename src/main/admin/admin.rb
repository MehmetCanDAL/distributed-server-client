require 'socket'
require_relative 'C:/Users/Asus/Desktop/22060351/proje/src/main/admin/src/main/proto/configuration_pb'

# Sunucu bilgileri
SERVERS = [
  { name: 'Server1', host: 'localhost', port: 5001 },
  { name: 'Server2', host: 'localhost', port: 5002 },
  { name: 'Server3', host: 'localhost', port: 5003 }
]

# Komut gönderme metodu
def send_command(server, method_type)
  begin
    socket = TCPSocket.new(server[:host], server[:port])
    config = Com::Example::Proto::Configuration.new( # Tam yolu belirtiyoruz
      fault_tolerance_level: 3,
      method: Com::Example::Proto::MethodType.const_get(method_type) # Enum için tam yol
    )
    socket.write(config.to_proto)
    socket.close
    puts "#{server[:name]} sunucusuna #{method_type} komutu gönderildi."
  rescue StandardError => e
    puts "Hata: #{server[:name]} ile bağlantı kurulamadı. #{e.message}"
  end
end

# Kullanıcı etkileşimi
loop do
  puts "\nKomut Seçin:"
  puts "1. Sunucuları Başlat (STRT)"
  puts "2. Sunucuları Durdur (STOP)"
  puts "3. Çıkış"
  print "Seçim: "
  choice = gets.chomp.to_i

  case choice
  when 1
    SERVERS.each { |server| send_command(server, 'STRT') }
  when 2
    SERVERS.each { |server| send_command(server, 'STOP') }
  when 3
    puts "Çıkış yapılıyor."
    break
  else
    puts "Geçersiz seçim. Tekrar deneyin."
  end
end
