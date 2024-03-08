#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <unistd.h>
#include <ctype.h>

// Define a structure for the message
struct msg_buffer {
    long msg_type;
    char msg_text[100];
};

int main() {
    key_t key;
    int msgid;

    // Generate a unique key for the message queue
    key = ftok("progfile", 65);

    // Create a message queue
    msgid = msgget(key, 0666 | IPC_CREAT);

    if (msgid == -1) {
        perror("msgget");
        exit(1);
    }

    pid_t pid = fork();

    if (pid < 0) {
        perror("Fork failed");
        exit(1);
    } else if (pid == 0) {
        // Child process

        struct msg_buffer msg;
        // Receive a message from the parent
        msgrcv(msgid, &msg, sizeof(msg.msg_text), 1, 0);
        printf("Child Process (PID: %d) received message: %s\n", getpid(), msg.msg_text);

        // Convert the message to uppercase
        for (int i = 0; i < strlen(msg.msg_text); i++) {
            msg.msg_text[i] = toupper(msg.msg_text[i]);
        }

        // Send the uppercase message back to the parent
        msg.msg_type = 2;
        msgsnd(msgid, &msg, sizeof(msg.msg_text), 0);
        printf("Child Process (PID: %d) sent uppercase response: %s\n", getpid(), msg.msg_text);
    } else {
        // Parent process

        struct msg_buffer msg;
        printf("Parent Process (PID: %d) is sending a message\n", getpid());

        // Send a message to the child
        msg.msg_type = 1;
        strcpy(msg.msg_text, "Hello, child process!");
        msgsnd(msgid, &msg, sizeof(msg.msg_text), 0);

        // Receive the response from the child
        msgrcv(msgid, &msg, sizeof(msg.msg_text), 2, 0);
        printf("Parent Process (PID: %d) received uppercase response: %s\n", getpid(), msg.msg_text);

        // Remove the message queue
        msgctl(msgid, IPC_RMID, NULL);
    }

    return 0;
}
