#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/*
 * Academic License - for use in teaching, academic research, and meeting
 * course requirements at degree granting institutions only.  Not for
 * government, commercial, or other organizational use.
 *
 * File: model.c
 *
 * Code generated for Simulink model 'model'.
 *
 * Model version                  : 1.352
 * Simulink Coder version         : 9.0 (R2018b) 24-May-2018
 * C/C++ source code generated on : Wed Aug 18 20:36:58 2021
 *
 * Target selection: ert.tlc
 * Embedded hardware selection: Intel->x86-64 (Mac OS X)
 * Code generation objectives:
 *    1. Execution efficiency
 *    2. RAM efficiency
 * Validation result: Not run
 */
#include<stdio.h>
#include "rtwtypes.h"

/* Named constants for Chart: '<Root>/CdPlayerBehaviorModel' */
#define IN_DiscPresent                 ((uint8_T)1U)
#define IN_Ejecting                    ((uint8_T)2U)
#define IN_Empty                       ((uint8_T)3U)
#define IN_FF                          ((uint8_T)1U)
#define IN_Inserting                   ((uint8_T)4U)
#define IN_NO_ACTIVE_CHILD             ((uint8_T)0U)
#define IN_PLAY                        ((uint8_T)2U)
#define IN_REW                         ((uint8_T)3U)
#define IN_STOP                        ((uint8_T)4U)

/* Named constants for Chart: '<Root>/CdPlayerModeManager' */
#define IN_AMMode                      ((uint8_T)1U)
#define IN_CDMode                      ((uint8_T)2U)
#define IN_Eject                       ((uint8_T)1U)
#define IN_FMMode                      ((uint8_T)3U)
#define IN_FastForward                 ((uint8_T)1U)
#define IN_ModeManager                 ((uint8_T)2U)
#define IN_Normal                      ((uint8_T)2U)
#define IN_ON                          ((uint8_T)1U)
#define IN_Play                        ((uint8_T)1U)
#define IN_Rew                         ((uint8_T)3U)
#define IN_Standby                     ((uint8_T)2U)
#define IN_Stop                        ((uint8_T)2U)

typedef enum {
  EMPTY,                        
  DISCINSERT,
  STOP,
  PLAY,
  REW,
  FF,
  EJECT
} CdRequestMode;

typedef enum {
  OFF,                        
  CD,
  FM,
  AM
} RadioRequestMode;

uint32_T temporalCounter_i1 = 0;
RadioRequestMode RadioReq_start;
uint8_T is_active_c1_model = 0;
uint8_T is_c1_model = 0;
uint8_T is_ModeManager;              
uint8_T was_ModeManager;             
uint8_T is_ON;                       
uint8_T was_ON;                      
uint8_T is_CDMode;                   
uint8_T is_Play;                     
uint8_T is_active_c4_model = 0;         
uint8_T is_c4_model = 0;                
uint8_T is_DiscPresent;              

RadioRequestMode RadioReq;           
CdRequestMode CdReq = EMPTY;              
boolean_T DiscEject;                
boolean_T DiscPresent;              
boolean_T DiscInsert;         

RadioRequestMode CurrentRadioMode = OFF;
CdRequestMode MechCmd = EMPTY;          
CdRequestMode CdStatus = EMPTY;         
boolean_T outDiscPresent;           

/* Forward declaration for local functions */
static void exit_internal_ON(void);
static void ModeManager(const RadioRequestMode *RadioReq_prev);
static void enter_internal_ModeManager(void);

/* Function for Chart: '<Root>/CdPlayerModeManager' */
static void exit_internal_ON(void)
{
  if (is_ON == IN_CDMode) {
    is_Play = IN_NO_ACTIVE_CHILD;
    is_CDMode = IN_NO_ACTIVE_CHILD;

    /* Outport: '<Root>/MechCmd' */
    MechCmd = STOP;
    is_ON = IN_NO_ACTIVE_CHILD;
  } else {
    is_ON = IN_NO_ACTIVE_CHILD;
  }
}

/* Function for Chart: '<Root>/CdPlayerModeManager' */
static void ModeManager(const RadioRequestMode *RadioReq_prev)
{
  /* Inport: '<Root>/DiscEject' */
  if (DiscEject != 0.0) {
    if (is_ModeManager == IN_ON) {
      exit_internal_ON();
      is_ModeManager = IN_NO_ACTIVE_CHILD;
    } else {
      is_ModeManager = IN_NO_ACTIVE_CHILD;
    }

    is_c1_model = IN_Eject;

    /* Outport: '<Root>/MechCmd' */
    MechCmd = EJECT;
  } else if (is_ModeManager == IN_ON) {
    /* Inport: '<Root>/RadioReq' */
    if (RadioReq == OFF) {
      exit_internal_ON();
      is_ModeManager = IN_Standby;
      was_ModeManager = IN_Standby;

      /* Outport: '<Root>/CurrentRadioMode' */
      CurrentRadioMode = OFF;

      /* Outport: '<Root>/MechCmd' */
      MechCmd = STOP;
    } else if (*RadioReq_prev != RadioReq_start) {
      if (RadioReq == CD) {
        exit_internal_ON();
        is_ON = IN_CDMode;
        was_ON = IN_CDMode;

        /* Outport: '<Root>/CurrentRadioMode' */
        CurrentRadioMode = CD;

        /* Outport: '<Root>/MechCmd' */
        MechCmd = STOP;

        /* Inport: '<Root>/DiscPresent' */
        if (DiscPresent) {
          is_CDMode = IN_Play;
          is_Play = IN_Normal;

          /* Outport: '<Root>/MechCmd' */
          MechCmd = PLAY;
        } else {
          is_CDMode = IN_Stop;

          /* Outport: '<Root>/MechCmd' */
          MechCmd = STOP;
        }
      } else if (RadioReq == AM) {
        exit_internal_ON();
        is_ON = IN_AMMode;
        was_ON = IN_AMMode;

        /* Outport: '<Root>/CurrentRadioMode' */
        CurrentRadioMode = AM;

        /* Outport: '<Root>/MechCmd' */
        MechCmd = STOP;
      } else {
        /* RadioReq==FM */
        exit_internal_ON();
        is_ON = IN_FMMode;
        was_ON = IN_FMMode;

        /* Outport: '<Root>/CurrentRadioMode' */
        CurrentRadioMode = FM;

        /* Outport: '<Root>/MechCmd' */
        MechCmd = STOP;
      }
    } else {
      switch (is_ON) {
       case IN_AMMode:
        /* Outport: '<Root>/CurrentRadioMode' */
        CurrentRadioMode = AM;
        break;

       case IN_CDMode:
        /* Outport: '<Root>/CurrentRadioMode' */
        /* Inport: '<Root>/CdReq' incorporates:
         *  Inport: '<Root>/DiscPresent'
         */
        CurrentRadioMode = CD;
        if (is_CDMode == IN_Play) {
          /* Inport: '<Root>/CdReq' */
          if (CdReq == STOP) {
            is_Play = IN_NO_ACTIVE_CHILD;
            is_CDMode = IN_Stop;

            /* Outport: '<Root>/MechCmd' */
            MechCmd = STOP;
          } else if ((CdReq == FF) && (is_Play != IN_FastForward)) {
            is_Play = IN_FastForward;

            /* Outport: '<Root>/MechCmd' */
            MechCmd = FF;
          } else if ((CdReq == REW) && (is_Play != IN_Rew)) {
            is_Play = IN_Rew;

            /* Outport: '<Root>/MechCmd' */
            MechCmd = REW;
          } else {
            if ((CdReq == PLAY) && (is_Play != IN_Normal)) {
              is_Play = IN_Normal;

              /* Outport: '<Root>/MechCmd' */
              MechCmd = PLAY;
            }
          }
        } else {
          if (DiscPresent && (CdReq == PLAY)) {
            is_CDMode = IN_Play;
            is_Play = IN_Normal;

            /* Outport: '<Root>/MechCmd' */
            MechCmd = PLAY;
          }
        }
        break;

       default:
        /* Outport: '<Root>/CurrentRadioMode' */
        CurrentRadioMode = FM;
        break;
      }
    }
  } else {
    /* Outport: '<Root>/CurrentRadioMode' */
    CurrentRadioMode = OFF;

    /* Inport: '<Root>/RadioReq' */
    if (RadioReq != OFF) {
      if (RadioReq == CD) {
        is_ModeManager = IN_ON;
        was_ModeManager = IN_ON;
        is_ON = IN_CDMode;
        was_ON = IN_CDMode;

        /* Outport: '<Root>/CurrentRadioMode' */
        CurrentRadioMode = CD;

        /* Outport: '<Root>/MechCmd' */
        MechCmd = STOP;

        /* Inport: '<Root>/DiscPresent' */
        if (DiscPresent) {
          is_CDMode = IN_Play;
          is_Play = IN_Normal;

          /* Outport: '<Root>/MechCmd' */
          MechCmd = PLAY;
        } else {
          is_CDMode = IN_Stop;

          /* Outport: '<Root>/MechCmd' */
          MechCmd = STOP;
        }
      } else if (RadioReq == AM) {
        is_ModeManager = IN_ON;
        was_ModeManager = IN_ON;
        is_ON = IN_AMMode;
        was_ON = IN_AMMode;

        /* Outport: '<Root>/CurrentRadioMode' */
        CurrentRadioMode = AM;

        /* Outport: '<Root>/MechCmd' */
        MechCmd = STOP;
      } else {
        /* RadioReq==FM */
        is_ModeManager = IN_ON;
        was_ModeManager = IN_ON;
        is_ON = IN_FMMode;
        was_ON = IN_FMMode;

        /* Outport: '<Root>/CurrentRadioMode' */
        CurrentRadioMode = FM;

        /* Outport: '<Root>/MechCmd' */
        MechCmd = STOP;
      }
    }
  }

  /* End of Inport: '<Root>/DiscEject' */
}

/* Function for Chart: '<Root>/CdPlayerModeManager' */
static void enter_internal_ModeManager(void)
{
  switch (was_ModeManager) {
   case IN_ON:
    is_ModeManager = IN_ON;
    was_ModeManager = IN_ON;
    switch (was_ON) {
     case IN_AMMode:
      is_ON = IN_AMMode;
      was_ON = IN_AMMode;

      /* Outport: '<Root>/CurrentRadioMode' */
      CurrentRadioMode = AM;

      /* Outport: '<Root>/MechCmd' */
      MechCmd = STOP;
      break;

     case IN_CDMode:
      is_ON = IN_CDMode;
      was_ON = IN_CDMode;

      /* Outport: '<Root>/CurrentRadioMode' */
      CurrentRadioMode = CD;

      /* Outport: '<Root>/MechCmd' */
      MechCmd = STOP;

      /* Inport: '<Root>/DiscPresent' */
      if (DiscPresent) {
        is_CDMode = IN_Play;
        is_Play = IN_Normal;

        /* Outport: '<Root>/MechCmd' */
        MechCmd = PLAY;
      } else {
        is_CDMode = IN_Stop;

        /* Outport: '<Root>/MechCmd' */
        MechCmd = STOP;
      }
      break;

     case IN_FMMode:
      is_ON = IN_FMMode;
      was_ON = IN_FMMode;

      /* Outport: '<Root>/CurrentRadioMode' */
      CurrentRadioMode = FM;

      /* Outport: '<Root>/MechCmd' */
      MechCmd = STOP;
      break;

     default:
      /* Inport: '<Root>/RadioReq' */
      if (RadioReq == CD) {
        is_ON = IN_CDMode;
        was_ON = IN_CDMode;

        /* Outport: '<Root>/CurrentRadioMode' */
        CurrentRadioMode = CD;

        /* Outport: '<Root>/MechCmd' */
        MechCmd = STOP;

        /* Inport: '<Root>/DiscPresent' */
        if (DiscPresent) {
          is_CDMode = IN_Play;
          is_Play = IN_Normal;

          /* Outport: '<Root>/MechCmd' */
          MechCmd = PLAY;
        } else {
          is_CDMode = IN_Stop;

          /* Outport: '<Root>/MechCmd' */
          MechCmd = STOP;
        }
      } else if (RadioReq == AM) {
        is_ON = IN_AMMode;
        was_ON = IN_AMMode;

        /* Outport: '<Root>/CurrentRadioMode' */
        CurrentRadioMode = AM;

        /* Outport: '<Root>/MechCmd' */
        MechCmd = STOP;
      } else {
        /* RadioReq==FM */
        is_ON = IN_FMMode;
        was_ON = IN_FMMode;

        /* Outport: '<Root>/CurrentRadioMode' */
        CurrentRadioMode = FM;

        /* Outport: '<Root>/MechCmd' */
        MechCmd = STOP;
      }

      /* End of Inport: '<Root>/RadioReq' */
      break;
    }
    break;

   case IN_Standby:
    is_ModeManager = IN_Standby;
    was_ModeManager = IN_Standby;

    /* Outport: '<Root>/CurrentRadioMode' */
    CurrentRadioMode = OFF;

    /* Outport: '<Root>/MechCmd' */
    MechCmd = STOP;
    break;

   default:
    is_ModeManager = IN_Standby;
    was_ModeManager = IN_Standby;

    /* Outport: '<Root>/CurrentRadioMode' */
    CurrentRadioMode = OFF;

    /* Outport: '<Root>/MechCmd' */
    MechCmd = STOP;
    break;
  }
}

/* Model step function */
void main(void)
{
  RadioRequestMode RadioReq_prev;

  /* Chart: '<Root>/CdPlayerModeManager' incorporates:
   *  Inport: '<Root>/RadioReq'
   */
  RadioReq_prev = RadioReq_start;
  RadioReq_start = RadioReq;
  if (is_active_c1_model == 0U) {
    is_active_c1_model = 1U;
    is_c1_model = IN_ModeManager;
    enter_internal_ModeManager();
  } else if (is_c1_model == IN_Eject) {
    is_c1_model = IN_ModeManager;
    enter_internal_ModeManager();
  } else {
    ModeManager(&RadioReq_prev);
  }

  /* End of Chart: '<Root>/CdPlayerModeManager' */

  /* Chart: '<Root>/CdPlayerBehaviorModel' incorporates:
   *  Inport: '<Root>/DiscInsert'
   *  Outport: '<Root>/MechCmd'
   */
  if (temporalCounter_i1 < MAX_uint32_T) {
    temporalCounter_i1++;
  }

  if (is_active_c4_model == 0U) {
    is_active_c4_model = 1U;
    is_c4_model = IN_Empty;

    /* Outport: '<Root>/CdStatus' */
    CdStatus = EMPTY;
  } else {
    switch (is_c4_model) {
     case IN_DiscPresent:
      if (MechCmd == EJECT) {
        is_DiscPresent = IN_NO_ACTIVE_CHILD;

        /* Outport: '<Root>/outDiscPresent' */
        outDiscPresent = false;
        is_c4_model = IN_Ejecting;
        temporalCounter_i1 = 0U;

        /* Outport: '<Root>/CdStatus' */
        CdStatus = EJECT;
      } else if (MechCmd == STOP) {
        is_DiscPresent = IN_STOP;

        /* Outport: '<Root>/CdStatus' */
        CdStatus = STOP;
      } else if (MechCmd == PLAY) {
        is_DiscPresent = IN_PLAY;

        /* Outport: '<Root>/CdStatus' */
        CdStatus = PLAY;
      } else if (MechCmd == REW) {
        is_DiscPresent = IN_REW;

        /* Outport: '<Root>/CdStatus' */
        CdStatus = REW;
      } else if (MechCmd == FF) {
        is_DiscPresent = IN_FF;

        /* Outport: '<Root>/CdStatus' */
        CdStatus = FF;
      } else {
        switch (is_DiscPresent) {
         case IN_FF:
          /* Outport: '<Root>/CdStatus' */
          CdStatus = FF;
          break;

         case IN_PLAY:
          /* Outport: '<Root>/CdStatus' */
          CdStatus = PLAY;
          break;

         case IN_REW:
          /* Outport: '<Root>/CdStatus' */
          CdStatus = REW;
          break;

         default:
          /* Outport: '<Root>/CdStatus' */
          CdStatus = STOP;
          break;
        }
      }
      break;

     case IN_Ejecting:
      /* Outport: '<Root>/CdStatus' */
      CdStatus = EJECT;
      if (temporalCounter_i1 >= 100U) {
        is_c4_model = IN_Empty;

        /* Outport: '<Root>/CdStatus' */
        CdStatus = EMPTY;
      }
      break;

     case IN_Empty:
      /* Outport: '<Root>/CdStatus' */
      CdStatus = EMPTY;
      if (DiscInsert) {
        is_c4_model = IN_Inserting;
        temporalCounter_i1 = 0U;

        /* Outport: '<Root>/CdStatus' */
        CdStatus = DISCINSERT;
      }
      break;

     default:
      /* Outport: '<Root>/CdStatus' */
      CdStatus = DISCINSERT;
      if (temporalCounter_i1 >= 100U) {
        is_c4_model = IN_DiscPresent;

        /* Outport: '<Root>/outDiscPresent' */
        outDiscPresent = true;
        is_DiscPresent = IN_STOP;

        /* Outport: '<Root>/CdStatus' */
        CdStatus = STOP;
      }
      break;
    }
  }
  /* End of Chart: '<Root>/CdPlayerBehaviorModel' */
}