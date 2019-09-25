#include <SoftwareSerial.h>
#include <Servo.h> 
Servo myservo; 
SoftwareSerial BTSerial(10, 11); // RX | TX
void setup() {
  myservo.attach(7);  // asignamos el pin 9 al servo.  
  Serial.begin(9600);
  Serial.println("Enter AT commands:");
 
  // HC-06 default serial speed is 9600
  BTSerial.begin(9600);
}

void loop() {
  if (BTSerial.available()) {
    Serial.write(BTSerial.read());
  }
 
  if (Serial.available()) {
    BTSerial.write(Serial.read());
  }
  int adc = 40;  // realizamos la lectura del potenciometro
  int angulo = map(adc, 0, 1023, 0, 180);  // escalamos la lectura a un valor entre 0 y 180 
  myservo.write(angulo);  // enviamos el valor escalado al servo.
  Serial.print("Angulo:  ");
  Serial.println(angulo);
  delay(100);  
    myservo.write(0);  // enviamos el valor escalado al servo.
   delay(100);  
}
