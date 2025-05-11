// Receiver Program (receiver.c)
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <winsock2.h>

#define PORT 8080
#define PACKET_SIZE 1024

void error(const char* msg) {
    fprintf(stderr, "%s: %d\n", msg, WSAGetLastError());
    exit(EXIT_FAILURE);
}

int main() {
    WSADATA wsa;
    SOCKET sockfd;
    struct sockaddr_in server_addr, client_addr;
    char buffer[PACKET_SIZE];
    int addr_len = sizeof(client_addr);

    // Initialize Winsock
    if (WSAStartup(MAKEWORD(2, 2), &wsa) != 0)
        error("WSAStartup failed");

    // Create UDP socket
    if ((sockfd = socket(AF_INET, SOCK_DGRAM, 0)) == INVALID_SOCKET)
        error("Socket creation failed");

    memset(&server_addr, 0, sizeof(server_addr));
    memset(&client_addr, 0, sizeof(client_addr));

    // Bind the socket
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = INADDR_ANY;

    if (bind(sockfd, (const struct sockaddr*)&server_addr, sizeof(server_addr)) == SOCKET_ERROR) {
        error("Bind failed");
    }
    printf("Receiver ready...\n");

    while (1) {
        int recv_len = recvfrom(sockfd, buffer, PACKET_SIZE - 1, 0, (struct sockaddr*)&client_addr, &addr_len);
        if (recv_len == SOCKET_ERROR) {
            error("Error receiving packet");
        }
        else {
            buffer[recv_len] = '\0'; // Null-terminate the received string
            printf("Received: %s\n", buffer);

            // Send ACK
            char ack[PACKET_SIZE];
            snprintf(ack, PACKET_SIZE, "ACK for %s", buffer);
            sendto(sockfd, ack, strlen(ack) + 1, 0, (struct sockaddr*)&client_addr, addr_len);
        }
    }

    closesocket(sockfd);
    WSACleanup();
    return 0;
}
