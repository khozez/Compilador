package Etapas;

import AccionesSemanticas.*;

public class MAS {
    public AccionSemantica[][] action_matrix = new AccionSemantica[18][29];

    public MAS() {
        //FILA 0
        for (int i=0; i<=2; i++){
            action_matrix[0][i]= new AS2();
        }
        for (int i=3; i<=13; i++){
            action_matrix[0][i]= new AS6();
        }
        action_matrix[0][14] = new AS13();
        action_matrix[0][15]= new AS6();
        action_matrix[0][16] = new AS13();
        action_matrix[0][17]= new AS6();
        for (int i=18; i<=23; i++){
            action_matrix[0][i]= new AS13();
        }
        for (int i=24; i<=27; i++){
            action_matrix[0][i]= new AS6();
        }
        action_matrix[0][28]= new ASg();


        //FILA 1
        for (int i=0; i<=9; i++){
            action_matrix[1][i]= new ASf();
        }
        action_matrix[1][10]= new AS6();
        action_matrix[1][11]= new AS6();
        for (int i=12; i<=23; i++){
            action_matrix[1][i]= new ASf();
        }
        action_matrix[1][24]= new AS6();
        for (int i=25; i<=28; i++){
            action_matrix[1][i]= new ASf();
        }


        //FILA 2
        action_matrix[2][0]= new AS4();
        action_matrix[2][1]= new AS4();
        action_matrix[2][2]= new AS4();
        action_matrix[2][3]= new AS6();
        action_matrix[2][4]= new AS6();
        for (int i=5; i<=9; i++){
            action_matrix[2][i]= new AS4();
        }
        action_matrix[2][10]= new AS6();
        for (int i=11; i<=28; i++){
            action_matrix[2][i]= new AS4();
        }


        //FILA 3
        for (int i=0; i<=9; i++){
            action_matrix[3][i]= new ASx();
        }
        action_matrix[3][10]= new ASs();
        for (int i=11; i<=15; i++){
            action_matrix[3][i]= new ASx();
        }
        action_matrix[3][16]= new AS6();
        action_matrix[3][17]= new AS6();
        for (int i=18; i<=28; i++){
            action_matrix[3][i]= new ASx();
        }


        //FILA 4
        for (int i=0; i<=9; i++){
            action_matrix[4][i]= new ASf();
        }
        action_matrix[4][10]= new AS6();
        for (int i=11; i<=28; i++){
            action_matrix[4][i]= new ASf();
        }


        //FILA 5
        for (int i=0; i<=4; i++){
            action_matrix[5][i]= new ASe();
        }
        action_matrix[5][5]= new AS6();
        action_matrix[5][6]= new AS11();
        for (int i=7; i<=28; i++){
            action_matrix[5][i]= new ASe();
        }


        //FILA 6
        for (int i=0; i<=6; i++){
            action_matrix[6][i]= new ASe();
        }
        action_matrix[6][7]= new AS5();
        for (int i=8; i<=28; i++){
            action_matrix[6][i]= new ASe();
        }


        //FILA 7
        for (int i=0; i<=2; i++){
            action_matrix[7][i]= new AS12();
        }
        action_matrix[7][3]= new AS6();
        for (int i=4; i<=8; i++){
            action_matrix[7][i]= new AS12();
        }
        action_matrix[7][9]= new AS6();
        action_matrix[7][10]= new AS12();
        action_matrix[7][11]= new AS6();
        for (int i=12; i<=28; i++){
            action_matrix[7][i]= new AS12();
        }


        //FILA 8
        for (int i=0; i<=8; i++){
            action_matrix[8][i]= new ASp();
        }
        action_matrix[8][9]= new AS6();
        for (int i=10; i<=28; i++){
            action_matrix[8][i]= new ASp();
        }


        //FILA 9
        for (int i=0; i<=2; i++){
            action_matrix[9][i]= new AS12();
        }
        action_matrix[9][3]= new AS6();
        for (int i=4; i<=8; i++){
            action_matrix[9][i]= new AS12();
        }
        action_matrix[9][9]= new AS6();
        for (int i=10; i<=28; i++){
            action_matrix[9][i]= new AS12();
        }


        //FILA 10
        for (int i=0; i<=16; i++){
            action_matrix[10][i]= new AS8();
        }
        action_matrix[10][17]= new AS7();
        for (int i=18; i<=28; i++){
            action_matrix[10][i]= new AS8();
        }


        //FILA 11
        for (int i=0; i<=3; i++){
            action_matrix[11][i]= new AS1();
        }
        for (int i=4; i<=8; i++){
            action_matrix[11][i]= new AS6();
        }
        action_matrix[11][9]= new AS1();
        action_matrix[11][10]= new AS6();
        action_matrix[11][11]= new AS6();
        for (int i=12; i<=28; i++){
            action_matrix[11][i]= new AS1();
        }


        //FILA 12
        for (int i=0; i<=14; i++){
            action_matrix[12][i]= new AS8();
        }
        action_matrix[12][15]= new AS9();
        for (int i=16; i<=28; i++){
            action_matrix[12][i]= new AS8();
        }


        //FILA 13
        action_matrix[13][0]= new AS9();
        action_matrix[13][1]= new AS9();
        action_matrix[13][2]= new AS3();
        for (int i=3; i<=28; i++){
            action_matrix[13][i]= new AS9();
        }


        //FILA 14
        for (int i=0; i<=26; i++){
            action_matrix[14][i]= new AS6();
        }
        action_matrix[14][27]= new AS10();
        action_matrix[14][28]= new AS6();


        //FILA 15
        for (int i=0; i<=11; i++){
            action_matrix[15][i]= new AS8();
        }
        action_matrix[15][12]= new AS7();
        for (int i=13; i<=28; i++){
            action_matrix[15][i]= new AS8();
        }


        //FILA 16
        for (int i=0; i<=12; i++){
            action_matrix[16][i]= new ASc();
        }
        action_matrix[16][13]= new AS7();
        for (int i=14; i<=28; i++){
            action_matrix[16][i]= new ASc();
        }


        //FILA 17
        for (int i=0; i<=9; i++){
            action_matrix[17][i]= new AS4();
        }
        action_matrix[17][10]= new AS6();
        for (int i=11; i<=28; i++){
            action_matrix[17][i]= new AS4();
        }
    }
}
