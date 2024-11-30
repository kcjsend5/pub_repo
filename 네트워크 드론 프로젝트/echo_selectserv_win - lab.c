#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <winsock2.h>
#define BUF_SIZE 1024
void ErrorHandling(char* message);

int main(void)
{
	WSADATA			wsaData;
	SOCKET			hServSock, hClntSock;
	SOCKADDR_IN		servAdr, clntAdr, clientaddr;
	int				adrSz;
	int				strLen, fdNum;
	int				position[21][21] = { 0, };
	unsigned char	buf[BUF_SIZE];			// 숫자 128이 넘어가면 숫자가 깨져서 나옴.
	int				xposition[BUF_SIZE];
	int				yposition[BUF_SIZE];
	int				addrlen;
	int				x, y;
	int				MaxRawNum = 20, MaxColNum = 20;

	if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0)
		ErrorHandling("WSAStartup() error!");

	hServSock = socket(PF_INET, SOCK_STREAM, 0);
	memset(&servAdr, 0, sizeof(servAdr));
	servAdr.sin_family = AF_INET;
	servAdr.sin_addr.s_addr = inet_addr("127.0.0.1");
	servAdr.sin_port = htons(9000);

	if (bind(hServSock, (SOCKADDR*)&servAdr, sizeof(servAdr)) == SOCKET_ERROR)
		ErrorHandling("bind() error");
	if (listen(hServSock, 5) == SOCKET_ERROR)
		ErrorHandling("listen() error");

	fd_set  reads, cpyReads;
	TIMEVAL  timeout;
	FD_ZERO(&reads);
	FD_SET(hServSock, &reads);

	memset(xposition, 0, BUF_SIZE);
	memset(yposition, 0, BUF_SIZE);

	while (1)
	{
		// select() 함수 호출
		cpyReads = reads;
		timeout.tv_sec = 5;
		timeout.tv_usec = 5000;

		fdNum = select(0, &cpyReads, 0, 0, &timeout);
		if (fdNum == SOCKET_ERROR) {
			closesocket(hClntSock);
			break;
		}
		else if (fdNum == 0) {
			continue;
		}
		for (int i = 0; i <= MaxColNum; i++) {
			for (int j = 0; j <= MaxRawNum; j++) {
				if (position[j][MaxColNum - i] == 1) {
					printf("o ");
				}
				else {
					printf("x ");
				}
			}
			printf("\n");
		}

		// signal이 온 상태에서 signal 온 핸들 확인 작업 수행 LOOP 생성.
		for (int i = 0; i < reads.fd_count; i++) {

			// signal 온 핸들 확인.
			if (FD_ISSET(reads.fd_array[i], &cpyReads)) {

				if (reads.fd_array[i] == hServSock) {
					// accept 실행...
					// client의 연결 요청 수신.
					adrSz = sizeof(clntAdr);
					printf("server> wait a client connection.\n");
					hClntSock = accept(hServSock, (SOCKADDR*)&clntAdr, &adrSz);
					printf("server> connected client: Port:%d, IP:%s \n",
						clntAdr.sin_port, inet_ntoa(clntAdr.sin_addr));

					FD_SET(hClntSock, &reads);
				}
				else
				{
					// data 수신.
					strLen = recv(reads.fd_array[i], buf, BUF_SIZE - 1, 0);
					if (strLen <= 0)    // close request!
					{
						closesocket(reads.fd_array[i]);
						FD_CLR(reads.fd_array[i], &reads);
						position[xposition[i]][yposition[i]] = 0; // 드론 위치 초기화
						xposition[i] = 0; // 드론 위치 삭제
						yposition[i] = 0;
						printf("server> close connection with (port:%d, IP:%s)\n", clientaddr.sin_port, inet_ntoa(clientaddr.sin_addr));
						break;
					}
					else
					{
						addrlen = sizeof(clientaddr);
						getpeername(reads.fd_array[i], (SOCKADDR*)&clientaddr, &addrlen);
						buf[strLen] = '\0';

						x = (int*)buf[1];//x,y좌표 입력
						y = (int*)buf[2];

						printf("\n현재 드론 위치 (%dm, %dm)\n", x, y);

						printf("server> (port:%d, IP:%s),Msg : %d,%d \n", clientaddr.sin_port, inet_ntoa(clientaddr.sin_addr), x, y);
						position[xposition[i]][yposition[i]] = 0; // 기존 드론 위치 초기화

						char ch;
						int dis;

						// buf[0]가 1이면 실행 o, 2이면 실행 x
						if (buf[0] == 1) {
							for (int i = 0; i < 2; i++) {
								printf("> 부호를 입력하시오(+/-): ");
								scanf("%c", &ch); // 부호입력
								printf("> %d번째 좌표의 이동거리를 입력하시오: ", i + 1);
								scanf("%d", &dis);// 이동거리 입력
								getchar(); // 이전 엔터 코드 제거
								if (i == 0) {
									if (ch == '+') {
										x += dis;
									}
									else if (ch == '-') {
										x -= dis;
									}
								}
								else {
									if (ch == '+') {
										y += dis;
									}
									else if (ch == '-') {
										y -= dis;
									}
								}
							}
						}

						buf[0] = x;
						buf[1] = y;

						int ddx = x / 10;
						int ddy = y / 10;

						xposition[i] = x / 10; // 드론 위치 기록
						yposition[i] = y / 10;

						position[ddx][ddy] = 1; // 드론 위치 재설정

						if (ddx > 20 || ddy > 20) {
							if (ddx > 20) {
								MaxRawNum = ddx + 5;
							}
							else {
								MaxColNum = ddy + 5;
							}

							int** graph = (int**)malloc((MaxRawNum + 1) * sizeof(int*));
							for (int i = 0; i <= MaxRawNum; i++) {
								graph[i] = (int*)malloc((MaxColNum + 1) * sizeof(int));
							}
						}

						send(reads.fd_array[i], (unsigned char*)buf, strlen(buf) + 1, 0);
					}
				}
			}
		}
	}
	closesocket(hServSock);
	WSACleanup();
	return 0;
}

void ErrorHandling(char* message)
{
	fputs(message, stderr);
	fputc('\n', stderr);
	exit(1);
}