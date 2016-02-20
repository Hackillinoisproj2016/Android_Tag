/*
 * IRremote: IRrecvDemo - demonstrates receiving IR codes with IRrecv
 * An IR detector/demodulator must be connected to the input RECV_PIN.
 * Version 0.1 July, 2009
 * Copyright 2009 Ken Shirriff
 * http://arcfn.com
 */

#include <IRremote.h>

int RECV_PIN = 11;
int LED_PIN = 3;

IRrecv irrecv(RECV_PIN);

decode_results results;

IRsend irsend;
boolean lightstate = 0;


void setup()
{
  pinMode(13, OUTPUT);
  irrecv.enableIRIn(); // Start the receiver
  
}

void loop() {
  ;
  if (irrecv.decode(&results)){
    
    if(lightstate == 0){
    digitalWrite(13, HIGH);
    lightstate = 1;
    irrecv.resume();
    }
    else{
      digitalWrite(13, LOW);
    lightstate =0;
    irrecv.resume();
    }
     
  }
  for (int i = 0; i < 3000; i++) {
    irsend.sendSony(0xa90, 12);
    delay(40);
  }
  delay(1000); //5 second delay between each signal burst
}
