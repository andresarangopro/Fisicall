void setup() {
  Serial1.begin(57600);
  Serial.begin(57600);
}
 
void loop() 
{  
    Serial1.write(Serial.read()+"s");
  while (Serial.available())
    Serial1.write(Serial.read());
  while (Serial1.available())
    Serial.write(Serial1.read());
}
