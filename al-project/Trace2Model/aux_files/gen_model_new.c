// ******************************************** Iteration:5

#include<stdio.h>
#include<stdbool.h>
#include<stdint.h>
void main()
{
    uint8_t event_seq_length = 3;
   uint8_t num_input = 1;
   uint8_t event_seq[1][3] = {{5,7,10}};
   uint8_t num_states = 4;
   uint8_t t[12][3];
   uint8_t count=0;
   uint8_t t_gen[9][3] = {{1,1,3},{2,6,3},{2,7,4},{2,9,2},{3,2,3},{3,3,4},{3,5,2},{4,4,3},{4,8,4}};

   for(uint8_t i=0;i<9;i++)             
        {                                                                       
            t[count][0] = t_gen[i][0];                                          
            t[count][1] = t_gen[i][1];                                          
            t[count][2] = t_gen[i][2];                                  
            count = count + 1;                                                  
        }
                                                                       
    for (uint8_t i=0;i<num_input;i++)                                                  
    {                                                                               
        uint8_t start_state_var;                                                        
        __CPROVER_assume(start_state_var <= num_states && start_state_var > 1);     
        //assert(start_state_var <= num_states);                                        

        t[count][0] = start_state_var;                                              
        for (uint8_t j=0;j<event_seq_length;j++)                                       
        {                                                                           
            uint8_t next_state_var;                                                     
            t[count][1] = event_seq[i][j];                                          
            if(event_seq[i][j] == 1)                                                
                t[count][0] = 1;                                                    
            __CPROVER_assume(next_state_var <= num_states && next_state_var > 1);   
            //assert(next_state_var <= num_states);                                 
            t[count][2] = next_state_var;                                           
            count = count+1;                                                            
            t[count][0] = t[count-1][2];                                            
        }                                                                           
    }

   bool in[4][10] = {false};                                               
    bool o[4][10] = {false};                                                        
                                                                                    
    for (uint8_t i=0;i<count;i++)                                                    
    {                                                                               
        o[t[i][0]-1][t[i][1]-1] = true;                                         
        in[t[i][2]-1][t[i][1]-1] = true;                                            
    }
                                                                       
    bool wrong_transition = false;                                                      
    for (uint8_t i=0; i<num_states;i++)                                                     
    {                                                                               
       if (in[i][ 0] && (o[i][6] || o[i][9]))
               wrong_transition = true;
   if (in[i][ 1] && (o[i][6] || o[i][9]))
               wrong_transition = true;
   if (in[i][ 2] && (o[i][4] || o[i][6]))
               wrong_transition = true;
   if (in[i][ 3] && (o[i][6] || o[i][9]))
               wrong_transition = true;
   if (in[i][ 4] && (o[i][1] || o[i][2] || o[i][3] || o[i][4] || o[i][7] || o[i][9]))
               wrong_transition = true;
   if (in[i][ 5] && (o[i][6] || o[i][9]))
               wrong_transition = true;
   if (in[i][ 6] && (o[i][1] || o[i][2] || o[i][4] || o[i][5] || o[i][6] || o[i][8]))
               wrong_transition = true;
   if (in[i][ 7] && (o[i][4] || o[i][6]))
               wrong_transition = true;
   if (in[i][ 8] && (o[i][4] || o[i][9]))
               wrong_transition = true;
   if (in[i][ 9] && (o[i][1] || o[i][2] || o[i][3] || o[i][4] || o[i][7] || o[i][9]))
               wrong_transition = true;
   }
   assert(wrong_transition != false);
}