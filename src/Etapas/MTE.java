package Etapas;

public class MTE {
    public int[][] states_matrix = new int[19][29];
    public static final int ERROR = -3;
    public static final int ESTADO_FINAL = -5;

    public MTE() {
        //FILA 0
        states_matrix[0][0] = 0;
        states_matrix[0][1] = 0;
        states_matrix[0][2] = 0;
        states_matrix[0][3] = 7;
        for (int i=4; i<=8; i++)
        {
            states_matrix[0][i] = 11;
        }
        states_matrix[0][9] = 7;
        states_matrix[0][10] = 1;
        states_matrix[0][11] = 11;
        states_matrix[0][12] = 15;
        states_matrix[0][13] = 16;
        states_matrix[0][14] = ESTADO_FINAL;
        states_matrix[0][15] = 12;
        states_matrix[0][16] = ESTADO_FINAL;
        states_matrix[0][17] = 10;
        for (int i=18; i<=23; i++)
        {
            states_matrix[0][i] = ESTADO_FINAL;
        }
        states_matrix[0][24] = 18;
        states_matrix[0][25] = 15;
        states_matrix[0][26] = 15;
        states_matrix[0][27] = 14;
        states_matrix[0][28] = ERROR;


        //FILA 1
        for (int i=0; i<=9; i++){
            states_matrix[1][i] = ERROR;
        }
        states_matrix[1][10] = 1;
        states_matrix[1][11] = 5;
        for (int i=12; i<=23; i++){
            states_matrix[1][i] = ERROR;
        }
        states_matrix[1][24] = 2;
        for (int i=25; i<=28; i++){
            states_matrix[1][i] = ERROR;
        }


        //FILA 2
        for (int i=0; i<=2; i++){
            states_matrix[2][i] = ESTADO_FINAL;
        }
        states_matrix[2][3] = 3;
        states_matrix[2][4] = 3;
        for (int i=5; i<=9; i++){
            states_matrix[2][i] = ESTADO_FINAL;
        }
        states_matrix[2][10] = 2;
        for (int i=11; i<=28; i++){
            states_matrix[2][i] = ESTADO_FINAL;
        }


        //FILA 3
        for (int i=0; i<=9; i++){
            states_matrix[3][i] = ERROR;
        }
        states_matrix[3][10] = 4;
        for (int i=11; i<=15; i++){
            states_matrix[3][i] = ERROR;
        }
        states_matrix[3][16] = 4;
        states_matrix[3][17] = 4;
        for (int i=18; i<=28; i++){
            states_matrix[3][i] = ERROR;
        }


        //FILA 4
        for (int i=0; i<=9; i++){
            states_matrix[4][i] = ERROR;
        }
        states_matrix[4][10] = 17;
        for (int i=11; i<=28; i++){
            states_matrix[4][i] = ERROR;
        }


        //FILA 5
        for (int i=0; i<=4; i++){
            states_matrix[5][i] = ERROR;
        }
        states_matrix[5][5] = 6;
        states_matrix[5][6] = ESTADO_FINAL;
        for (int i=7; i<=28; i++){
            states_matrix[5][i] = ERROR;
        }


        //FILA 6
        for (int i=0; i<=6; i++){
            states_matrix[6][i] = ERROR;
        }
        states_matrix[6][7] = ESTADO_FINAL;
        for (int i=7; i<=28; i++){
            states_matrix[6][i] = ERROR;
        }


        //FILA 7
        for (int i=0; i<=2; i++){
            states_matrix[7][i] = ESTADO_FINAL;
        }
        states_matrix[7][3] = 7;
        for (int i=4; i<=8; i++){
            states_matrix[7][i] = ESTADO_FINAL;
        }
        states_matrix[7][9] = 7;
        states_matrix[7][10] = ESTADO_FINAL;
        states_matrix[7][11] = 7;
        for (int i=12; i<=28; i++){
            states_matrix[7][i] = ESTADO_FINAL;
        }

        //FILA 10
        for (int i=0; i<=28; i++){
            states_matrix[10][i] = ESTADO_FINAL;
        }


        //FILA 11
        for (int i=0; i<=3; i++){
            states_matrix[11][i] = ESTADO_FINAL;
        }
        for (int i=4; i<=8; i++){
            states_matrix[11][i] = 11;
        }
        states_matrix[11][9] = ESTADO_FINAL;
        states_matrix[11][10] = 11;
        states_matrix[11][11] = 11;
        for (int i=12; i<=28; i++){
            states_matrix[11][i] = ESTADO_FINAL;
        }


        //FILA 12
        for (int i=0; i<=14; i++){
            states_matrix[12][i] = ESTADO_FINAL;
        }
        states_matrix[12][15] = 13;
        for (int i=16; i<=28; i++){
            states_matrix[12][i] = ESTADO_FINAL;
        }


        //FILA 13
        for (int i=0; i<=1; i++){
            states_matrix[13][i] = 13;
        }
        states_matrix[13][2] = 0;
        for (int i=3; i<=28; i++){
            states_matrix[13][i] = 13;
        }


        //FILA 14
        for (int i=0; i<=26; i++){
            states_matrix[14][i] = 14;
        }
        states_matrix[14][27] = ESTADO_FINAL;
        states_matrix[14][28] = 14;


        //FILA 15
        for (int i=0; i<=28; i++){
            states_matrix[15][i] = ESTADO_FINAL;
        }


        //FILA 16
        for (int i=0; i<=12; i++){
            states_matrix[16][i] = ERROR;
        }
        states_matrix[16][13] = ESTADO_FINAL;
        for (int i=14; i<=28; i++){
            states_matrix[16][i] = ERROR;
        }


        //FILA 17
        for (int i=0; i<=9; i++){
            states_matrix[17][i] = ESTADO_FINAL;
        }
        states_matrix[17][10] = 17;
        for (int i=11; i<=28; i++){
            states_matrix[17][i] = ESTADO_FINAL;
        }


        //FILA 18
        for (int i=0; i<=2; i++){
            states_matrix[18][i] = ESTADO_FINAL;
        }
        states_matrix[18][3] = 3;
        states_matrix[18][4] = 3;
        for (int i=5; i<=9; i++){
            states_matrix[18][i] = ESTADO_FINAL;
        }
        states_matrix[18][10] = 2;
        for (int i=11; i<=28; i++){
            states_matrix[18][i] = ESTADO_FINAL;
        }
    }

    public void show(){
        System.out.println("	MATRIZ DE TRANSICION DE ESTADOS");
        for (int i = 0; i < states_matrix.length; i++) {
            for (int j = 0; j < states_matrix[i].length; j++) {
                System.out.print(states_matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}