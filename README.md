# Dağıtık Abonelik Sistemi (Distributed Subscriber Service)

Bu paragrafta hata tolerans sisteminde sunucu tercihi veya özgün katkılarınızı ifade edebilirsiniz.


### ServerX.java özellikleri

- [ ] admin_client.rb ile başlama
- [ ] hata toleransı 1 prensibiyle çalışma
- [x] hata toleransı 2 prensibiyle çalışma
- admin.rb den STRT komutunu alarak istemciden gelen komuta göre client'ları dinlemeye ve diğer serverlara bağlanıyor
- client.java'dan gelen abone nesnesini alıyor
- kendi içindeki abone bilgilerini diğer 2 server ile paylaşıyor

### Client.java özellikleri

- subscriber.proto dan derlenen proto ile abone nesnesi oluşturarak serverx.java'ya gönderir

### admin.rb özellikleri

- configuration.proto dan derlenen configuration_pb.proto'dan method okur ona göre STRT emrini veya STOP emrini serverlara göderir onu da protobuf üzerinden yapıyor

##gitgub link

- https://github.com/MehmetCanDAL/distributed-server-client.git

### Ekip üyeleri

- 22060351, Mehmet Can Dal
- Numara, İsim
- Numara, İsim
- Numara, İsim
