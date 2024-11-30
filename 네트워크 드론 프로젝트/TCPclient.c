#include <winsock2.h>
#include <stdlib.h>
#include <stdio.h>
#define	BUF_SIZE	512

void ErrorDisplay(char* str)
{
	printf("<ERROR> %s!!!\n", str);
	exit(-1);
}

// 사용자 정의 데이터 수신 함수
int recvn(SOCKET s, char* buf, int len, int flags)
{
	int 	received;
	char* ptr = buf;
	int 	left = len;


	while (left > 0) {
		received = recv(s, ptr, left, flags);
		if (received == SOCKET_ERROR)
			return SOCKET_ERROR;
		else if (received == 0)
			break;
		left -= received;
		ptr += received;
	}
	return (len - left);
}


int main(int argc, char* argv[])
{
	int				retval;
	int				MaxRawNum = 20, MaxColNum = 20;
	int				graph[21][21] = { 0, };
	srand(time(NULL));

	WSADATA	wsa;
	retval = WSAStartup(MAKEWORD(2, 2), &wsa);
	if (retval != 0)	return -1;

	SOCKET	ClientSocket;
	ClientSocket = socket(AF_INET, SOCK_STREAM, 0);
	if (ClientSocket == INVALID_SOCKET) {
		ErrorDisplay("socket() error(INVALID_SOCKET)");
	}

	SOCKADDR_IN	ServerAddr;
	ZeroMemory(&ServerAddr, sizeof(ServerAddr));
	ServerAddr.sin_family = AF_INET;
	ServerAddr.sin_port = htons(9000);
	ServerAddr.sin_addr.s_addr = inet_addr("127.0.0.1");

	retval = connect(ClientSocket, (SOCKADDR*)&ServerAddr, sizeof(ServerAddr));
	if (retval == SOCKET_ERROR) {
		ErrorDisplay("connect() error(SOCKET_ERROR)");
	}

	// char으로 하면 숫자 128이 넘어가면 숫자가 깨져서 나옴.
	unsigned char	Buf[BUF_SIZE + 1];

	// 처음 드론 위치
	int				x = rand() % 201;
	int				y = rand() % 181 + 20;

	printf("드론 위치 (%dm, %dm)\n", x, y);
	//int		iLen;
	while (1)
	{
		ZeroMemory(Buf, sizeof(Buf));

		Buf[0] = 1;
		Buf[1] = x;
		Buf[2] = y;

		retval = send(ClientSocket, Buf, strlen(Buf), 0);
		if (retval == SOCKET_ERROR) {
			printf("<ERROR> send()(SOCKET_ERROR)!!!\n");
			break;
		}

		retval = recvn(ClientSocket, Buf, sizeof(int), 0);

		if (retval == SOCKET_ERROR) {
			printf("<ERROR> recvn()(SOCKET_ERROR)!!!\n");
			break;
		}
		else if (retval == 0)	break;

		x = Buf[0];
		y = Buf[1];

		printf("\n이동한 드론 위치 (%dm, %dm)\n", x, y);
	}


	closesocket(ClientSocket);
	WSACleanup();
	return 0;
}