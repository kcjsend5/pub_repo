

public class tree {

    public static void main(String[] args) {

        double p[] = {0,0.04,0.03,0.05,0.03,0.15,0.15,0.15};
        double q[] = {0.04,0.04,0.03,0.05,0.04,0.1,0.05,0.15};
        double w[][] = new double[8][8];
        double c[][] = new double[8][8];
        int r[][] = new int [8][8];
        for(int i = 0; i<=7;i++){
            w[i][i] = q[i];
            c[i][i] = 0;
        }
        for(int x = 1; x<=7;x++){
            for(int i = 0; i<=7-x;i++){
                int j = i + x;
                w[i][j] = w[i][j-1]+p[j]+q[j];
                for(int k = i+1;k<=j;k++){
                    double temp = c[i][k-1]+c[k][j]+w[i][j];
                    if(k == i+1){
                        c[i][j]=temp;
                        r[i][j] = k;
                    }
                    else if(temp < c[i][j]){
                        c[i][j] = temp;
                        r[i][j] = k;
                    }
                }
            }
        }
        System.out.println("\n가중치");
        for(int i = 0; i<=7;i++){
            for(int j = 0; j<=7;j++){
                System.out.print(w[i][j]+" ");
            }
            System.out.println(" ");
        }
        System.out.println("\n비용");
        for(int i = 0; i<=7;i++){
            for(int j = 0; j<=7;j++){
                System.out.print(c[i][j]+" ");
            }
            System.out.println(" ");
        }
        System.out.println("\nroot");
        for(int i = 0; i<=7;i++){
            for(int j = 0; j<=7;j++){
                System.out.print(r[i][j]+" ");
            }
            System.out.println(" ");
        }

        System.out.println("\ntree");
        System.out.println("\t"+"\t"+r[0][7]+"\t"+"\t");
        System.out.println("\t"+r[0][4]+"\t"+"\t"+r[5][7]+"\t");
        System.out.println("  "+r[0][2]+"   "+r[3][4]+"\t  "+r[5][6]);
        System.out.println("   "+r[1][2]);

    }

}
