#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/msg.h>
#include <sys/ipc.h>
#include <error.h>
#include <ctype.h>
#include <unistd.h>


#define MAX_MSG_SIZE 100

struct msg {
    long int msg_type;
    char msg_text[MAX_MSG_SIZE];
};

// Function to convert to uppercase
void to_uppercase(char *str){
    while (*str){
        *str = toupper((unsigned char) *str);
        str++;
    }
}

int main(){
    int msg_id;
    struct msg message;
    key_t key;
    pid_t pid;
    long int msg_to_rcv = 0;

    // Generate a key for the message queue
    // IMPORTANT: file.txt was created because using progfile did not work for the linux labs
    if ((key = ftok("file.txt", 65)) == -1) {
        perror("Key generation failed");
        return -1;
    }
    

    // Creates a message queue in memory with permissions 0666 and returns an identifier
    msg_id = msgget(key, 0666 | IPC_CREAT);
    if (msg_id == -1){
        perror("msgget");
        return -1;
    }


    pid = fork();

    if (pid < 0){
        perror("Fork Failed");
        return -1;
    } else if (pid == 0){
        // Child process

        // Wait to recieve message and print what is received
        msgrcv(msg_id, (void *)&message, MAX_MSG_SIZE, msg_to_rcv, 0);
        printf("Child received: %s\n", message.msg_text);
        
        // Convert to uppercase
        to_uppercase(message.msg_text);
     
        // Send the modified message back to parent
        message.msg_type = 1;

        // Send message and print what is sent 
        if (msgsnd(msg_id, (void *)&message, MAX_MSG_SIZE, 0) == -1){
            perror("msgsnd");
        }
        else{
            printf("Child sent: %s\n", message.msg_text);
        }

    } else {
        // Parent process

        // Set message type
        message.msg_type = 1;

        // Copy string to struct
        strcpy(message.msg_text, "Hello, child process!");

        // Send message and print what is sent
        if (msgsnd(msg_id, (void *)&message, MAX_MSG_SIZE, 0) == -1){
            perror("msgsnd");
        }
        else{
            printf("Parent sent: %s\n", message.msg_text);
        }

        // Wait and recieve the message from child
        // Print what parent received
        msgrcv(msg_id, (void *)&message, MAX_MSG_SIZE, msg_to_rcv, 0);
        printf("Parent received: %s\n", message.msg_text);


    }

    msgctl(msg_id, IPC_RMID, 0);
    return 0;
}
