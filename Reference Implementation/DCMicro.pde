
typedef enum 
{
  STATE_IDLE,
  STATE_INIT1,
  STATE_INIT2,
  STATE_SESSION,
  STATE_IDLE_W,
  STATE_INIT1_W,
  STATE_INIT2_W,
  STATE_SESSION_W
} states;

char* DEVICE_KEY = "ZZZZZZZZZZZZZZZZZZZZ";
char* tickScale = "0035";  //70cm ~= 26"  //2 ticks per rotation
long sessionTimeoutMillis = 180000;// = 120000;
long commTimeoutMillis = 120000;// = 60000;
int sessionStartTicks = 10;
int PIN_CYCLE = 2;
int PIN_CYCLE_ALT = 3;
int PIN_CYCLE_ANALOG = 5;
int PIN_LED = 13;

int currentState;
long tickQueue;
long sessionStartTime;
long lastTickTime;
long lastByteTime;
bool testMode = false;

void setup()
{
  pinMode(PIN_CYCLE, INPUT);  
  pinMode(PIN_LED, OUTPUT);
  beginSerial(19200);

  stateIdle();
  blinkInit();
  
    
 /* while(true)  
  {
    int valfour = analogRead(4);
    int valfive = analogRead(5);
    if (valfour > 800 || valfive > 800){
      Serial.print(valfour);
      Serial.print("    ");
      Serial.print(valfive);
      Serial.println("");
    }
  }*/
  
  
  
  attachInterrupt(PIN_CYCLE-2, cycleInterrupt, RISING);  //pin 2 interrupt 0, pin 3 is interrupt 1
}

void loop()
{
   
  //send outputs
  sendOutput();
  //transition states
  if (Serial.available())
  {
    transitionState();
  }
  //comm timeout
  if (currentState == STATE_IDLE_W
      || currentState == STATE_INIT1_W
      || currentState == STATE_INIT2_W
      || currentState == STATE_SESSION_W)
  {
    if (millis() - lastByteTime > commTimeoutMillis)
    {
      Serial.print('C');
      lastByteTime = millis();
      stateIdle();
    }
  }
  
  /*if (analogRead(5) > 1000)  //non-interrupt method
  {  
    cycleInterrupt();
  }*/
  //delay(100);
}

void sendOutput()
{
  switch (currentState)
  {
  case STATE_IDLE:
    //send 'I' if 10 ticks in < 30 secs

    if (testMode)
    {
      Serial.print('I');
      currentState = STATE_IDLE_W;
    }
    else if (tickQueue > sessionStartTicks)
    {
      Serial.print('I');
      currentState = STATE_IDLE_W;
    }

    break;
  case STATE_INIT1:
    Serial.print('K');
    Serial.print(DEVICE_KEY);
    currentState = STATE_INIT1_W;
    break;
  case STATE_INIT2:
    Serial.print('S');
    Serial.print(tickScale);  //20cm per tick. TODO: calculate this
    currentState = STATE_INIT2_W;
    break;
  case STATE_SESSION:
    if (tickQueue > 0)
    {
      Serial.print('T');
      digitalWrite(PIN_LED, LOW);
      tickQueue--;
      currentState = STATE_SESSION_W;
    }

    /*
      Serial.print("Now: ");
     Serial.println(millis());
     Serial.print("lastTickTime: ");
     Serial.println(lastTickTime);
     Serial.print("delta: ");
     Serial.println(millis() - lastTickTime);
     Serial.println("");
     */

    //cycle inactive timeout
    int timeDiff = millis() - lastTickTime;
    if (timeDiff > 0 && timeDiff > sessionTimeoutMillis)
    {
      Serial.print('Q');
      currentState = STATE_SESSION_W;  //TODO: test this
    }
    break;
  }
}

void stateIdle()
{
  currentState = STATE_IDLE;
  tickQueue = 0;
  digitalWrite(PIN_LED, LOW);
}

void stateInit1()
{
  currentState = STATE_INIT1;
  digitalWrite(PIN_LED, LOW);
}

void stateInit2()
{
  currentState = STATE_INIT2;
  digitalWrite(PIN_LED, LOW);
}

void stateSession(bool newSession)
{
  currentState = STATE_SESSION;

  if (newSession)
  {
    sessionStartTime = millis();
    lastTickTime = millis();
    tickQueue = 0; //reset pre-session ticks 
  }

  digitalWrite(PIN_LED, HIGH);
}

void cycleInterrupt()
{
  //delay(10);
  int digitalVal = digitalRead(PIN_CYCLE);
  int digitalValAlt = digitalRead(PIN_CYCLE_ALT);
//  int analogVal = analogRead(PIN_CYCLE_ANALOG);
  
 // Serial.print(digitalVal);
 // Serial.print("  ");
 // Serial.print(analogVal);
 // Serial.println("");
  
  if (digitalValAlt == HIGH && digitalVal == HIGH)
  {
    long currentTime = millis();
    if ((currentTime - lastTickTime) > 10)  //TODO: adjust noise threshold
    {
      tickQueue++;
      lastTickTime = currentTime;
    }
  }
  //Serial.println(tickQueue);
}

void blinkInit()
{
  int numBlinks=5;
  for (int i=0; i<numBlinks*2;i++){
    int state;
    if (i%2 == 0) 
      state = HIGH;
    else 
      state = LOW;
    digitalWrite(PIN_LED, state);
    delay(200);
  }
}

void transitionState()
{
  int retState = STATE_IDLE;
  int numSerChars = Serial.available();

  char c1 = (char) Serial.read();
  lastByteTime = millis();

  if (c1 == 'e' || c1 == 'q')
  {
    stateIdle();
  }

  switch (currentState)
  {
  case STATE_IDLE_W:
    //if we receive i0 or e go to STATE_IDLE
    //if we receive i1 go to STATE_INIT1
    
    if (c1 == 'i'); 
    {  
      while (Serial.available() == 0) 
      {
        delay(5);  //wait for the second char
      }
      char c2 = (char) Serial.read();
      if (c2 == '0')
      {
        stateIdle();
      }
      else if (c2 == '1')
      {
        stateInit1();
      }
    }
    break;

  case STATE_INIT1_W:
    //if we receive k0 or e go to STATE_IDLE
    //if we receive k1 go to STATE_INIT2
    if (c1 == 'k'); 
    {  
      while (Serial.available() == 0) 
      {
        delay(5);  //wait for the second char, long delay for db query
      }
      char c2 = (char) Serial.read();
      if (c2 == '0')
      {
        stateIdle();
      }
      else if (c2 == '1')
      {
        stateInit2();
      }
    }
    break;

  case STATE_INIT2_W:
    //if we recieve s0 or e go to STATE_IDLE
    //if we receive s1 go to STATE_SESSION

    if (c1 == 's'); 
    {  
      while (Serial.available() == 0) 
      {
        delay(5);  //wait for the second char
      }
      char c2 = (char) Serial.read();
      if (c2 == '0')
      {
        stateIdle();
      }
      else if (c2 == '1')
      {
        char data[17]; //get the next 21 chars
        while (Serial.available() == 0) 
        {
          delay(5);  //wait 
        }
        for (int i=0; i<17;i++)
        {
          while (Serial.available() == 0)
          {
            delay(20);
          }
          data[i] = (char) Serial.read();
        }
        
        // retrieve and set the start ticks, sess timeout, comm timeout
        //char 1,2 tick num
        //char 4,5,6,7,8,9 session timeout
        //char 11,12,13,14,15,16 comm timeout
        char numTickStr[3];
        char* pChar1 = &data[1];
        strncpy(numTickStr, pChar1, 2); 
        numTickStr[2] = '\0';
        char sessTimeoutStr[7];
        char* pChar2 = &data[4];
        strncpy(sessTimeoutStr, pChar2, 6); 
        sessTimeoutStr[6] = '\0';
        char commTimeoutStr[7];
        char* pChar3 = &data[11];
        strncpy(commTimeoutStr, pChar3, 6);
        commTimeoutStr[6] = '\0';
        
        sessionStartTicks = atoi(numTickStr);
        sessionTimeoutMillis = atol(sessTimeoutStr);
        commTimeoutMillis = atol(commTimeoutStr);
                                        
        stateSession(true);
      }
    }
    break;

  case STATE_SESSION_W:
    //if we receive q, o, or e go to STATE_IDLE
    //if we receive t go to STATE_SESSION

    if (c1 == 'o' || c1 == 'q')
    {  
      stateIdle();
    }
    else if (c1 == 't')
    {
      stateSession(false);
    }
    break;

  default:
    stateIdle();
    break;
  } 
}
