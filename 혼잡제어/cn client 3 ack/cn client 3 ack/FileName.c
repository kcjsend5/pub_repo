// Sender Program (sender.c)
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <winsock2.h>

#define PORT 8080
#define PACKET_SIZE 1024
#define TIMEOUT_SEC 2

void error(const char* msg) {
    fprintf(stderr, "%s: %d\n", msg, WSAGetLastError());
    exit(EXIT_FAILURE);
}

int main() {
    WSADATA wsa;
    SOCKET sockfd;
    struct sockaddr_in server_addr;
    char packet[PACKET_SIZE], ack[PACKET_SIZE], temp[PACKET_SIZE];
    int server_addr_len = sizeof(server_addr);
    int cnt;

    // Initialize Winsock
    if (WSAStartup(MAKEWORD(2, 2), &wsa) != 0)
        error("WSAStartup failed");

    // Create UDP socket
    if ((sockfd = socket(AF_INET, SOCK_DGRAM, 0)) == INVALID_SOCKET)
        error("Socket creation failed");

    memset(&server_addr, 0, sizeof(server_addr));

    // Server address setup
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = inet_addr("127.0.0.1"); // Receiver's IP address

    int cwnd = 1; // Congestion window size in packets
    int threshold = 16; // Initial threshold

    while (1) {
        cnt = 1;
        printf("Sending %d packets...\n", cwnd);
        for (int i = 0; i < cwnd; i++) {
            // Set the packet content
            snprintf(packet, PACKET_SIZE, "Test Packet %d", i + 1); // Safe formatting with size limit
            if (cnt == 3) {
                strcpy(packet ,ack);
                cnt = 1;    
                i--;
            }
            // Send the packet
            int sent = sendto(sockfd, packet, strlen(packet) + 1, 0, (struct sockaddr*)&server_addr, sizeof(server_addr));
            if (sent == SOCKET_ERROR) {
                error("Error sending packet");
            }
            else {
                printf("Sent: %s\n", packet);
            }

            // Wait for ACK
            struct timeval timeout = { TIMEOUT_SEC, 0 };
            fd_set read_fds;
            FD_ZERO(&read_fds);
            FD_SET(sockfd, &read_fds);

            int retval = select(0, &read_fds, NULL, NULL, &timeout);
            if (retval > 0) {
                recvfrom(sockfd, ack, PACKET_SIZE, 0, (struct sockaddr*)&server_addr, &server_addr_len);
                printf("Received: ACK for %s\n", ack);
                if (!strcmp(ack,temp)) {
                    cnt++;
                }
                strcpy(temp, ack);
                if (cnt == 3) {
                    threshold = cwnd / 2;
                    cwnd = cwnd / 2 + 3;
                    printf("<<< 3-DUP ACK 사건 발생 >>>\n");
                    printf("- cwnd: cwnd/2+3으로 조정 -> %d\n", cwnd);
                    printf("- 임계값: %d로 설정\n",threshold);
                    memset(temp,0, PACKET_SIZE * sizeof(char));
                }
            }
            else {
                printf("Timeout waiting for ACK. Resending...\n");
            }
            Sleep(1000);
        }

        // Adjust congestion window
        cwnd = (cwnd < threshold) ? cwnd * 2 : cwnd + 1;
        if (cwnd > 32) cwnd = 1; // Reset for demo purposes
    }

    closesocket(sockfd);
    WSACleanup();
    return 0;
}