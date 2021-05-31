#include <SoftTimer.h>
char data = 0;              //Variabel för lagring av mottagen data
void callBack2(Task* me);
Task t2(100, callBack2);      //Uppdatera knapptryckning för buzzer varje 0,1 sekunder (100 ms)
void setup() 
{
  Serial.begin(9600);         //datahastighet inställd i bits per sekund för seriell dataöverföring
  pinMode(13, OUTPUT);        //Ställer in digitala pin 13 som utgångspin
  SoftTimer.add(&t2);         //Lägger till timers 1 för buzzer, och 2 för bluetooth-signal
}
void callBack2(Task* me) 
{
  if(Serial.available() > 0)   //Skicka endast data när data tas emot
  {
    data = Serial.read();      //Läs inkommande data och lagra den i variabel data
    Serial.print(data);        //Skriv ut värde inuti data i seriell monitor
    Serial.print("\n");        //Ny rad
    if(data == '1')           //Kontrollerar om värdet på data är lika med 1
    {
      tone(13, 920);           //Spelar ton tre gånger
      delay(1000);
      noTone(13);
      delay(500);
      tone(13, 920);
      delay(1000);
      noTone(13);
      delay(500);
      tone(13, 920);
      delay(1000);
      noTone(13);
      delay(500);
      data = 0;
    } 
  }
}
