package omok;

public class Board {
    int size;
    String[][] map;
    
    int []dx={1,0,1,-1};                              //순서대로 아래, 위, 오른쪽 아래 대각선, 왼쪽 아래 대각선 체크 
    int []dy={0,1,1,1};   
    
    public Board(int size) {
        this.size = size;
        
        map = new String[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                map[row][col] = ".";
            }
        }
    }
    
    public void print() {
       
        for (int row = 0; row < size; row++)
        {
            System.out.printf("%2d",row);
            for (int col = 0; col < size; col++)
            {
                System.out.print(" " + map[row][col]);
            }
            System.out.println();
        }
        
        System.out.print("  ");
        for(int i=65; i<=83; i++)
        {
            System.out.print(" "+(char)i);
        }
        System.out.println();
        System.out.println();
    }
    
    public void put(Player player,int row,int col)       //사용자라면 O 표시, 컴퓨터라면 X 표시
    {
        map[row][col]= player.stone;
    }
    
    public boolean possible(int row, int col) 
    { 
        
       if(row>=19 || row<0 || col>=19 || col<0)         //19x19 범위를 벗어났다면 false반환 (row,col 모두 0~19행, 0~19열 사이에 존재해야함)
        {
            return false;
        }
        
       if(!(".").equals(map[row][col]))                 //이미 돌이 놓여진 위치라면 false 반환
        {
            return false;
        }
        
       return true;                                     //모두 만족한다면 true 반환
    }
    
    public boolean check(int x,int y)
    {
        int cnt=0;                                      //카운트 선언
        int flag=0;
        
        for(int k=0; k<4; k++)                          //4방향 탐색 시작
        {
            cnt=1;                                      //현재 놓은 돌부터 세기 때문에 1로 시작
            
            int nx=x;                                   //nx, ny에 입력한 좌표 x,y 대입
            int ny=y;
            
            while(true)
            {
                nx+=dx[k];                              //현재 좌표에 같은 방향으로 1전진
                ny+=dy[k];                 
                
                if(0<=nx && nx<19 && 0<=ny && ny<19)    //전진한 좌표가 판 안에 있는지 확인
                {
                    if(map[x][y].equals(map[nx][ny]))   //입력한 좌표의 돌이 전진한 좌표의 돌과 같은 종류라면 (사용자는 O, 컴퓨터는 X)
                    {
                        cnt++;                          //개수 증가
                    }
                    else
                    {
                        break;
                    }
                }
                else                                    //전진한 값이 판 안에 있는 값이 아니라면 break 하고 while문 반복 -> 새로운 방향 탐색
                {
                    break;
                }
            }
            
            nx=x;                                       //다시 nx, ny에 처음 입력한 좌표 대입
            ny=y;
            
            while(true)                                 //반대 방향 탐색
            {
                nx-=dx[k];                              //음수를 사용해서 부호 바꿈
                ny-=dy[k];
                
                if(0<=nx && nx<19 && 0<=ny && ny<19)
                {
                    if(map[x][y].equals(map[nx][ny]))
                    {
                        cnt++;
                    }
                    else
                    {
                        break;
                    }
                }
                else
                {
                    break;
                }
            }
            if(cnt==5)
            {
                flag=1;                                //카운트가 5가 되면 더 이상 확인할 필요없이 종료 flag 값을 1로 변환
                break;
            }
        }
        
        if(flag==1)                                    //카운트가 5라는 뜻이므로 true 반환
        {
            return true;
        }
        else {
            return false;
        }
    }
}