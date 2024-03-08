#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

int main() {
    pid_t pid;
    // id and fork
    pid = fork();

    if (pid < 0) {
        // Error
        perror("Fork Failed");
        return 1;
    } else if (pid == 0) {
        // Child execute
        printf("I am the child process. PID: %d\n", getpid());
    } else {
        // Parent execute
        // Wait for child to finish running
        //wait(NULL);
        printf("I am the parent process. PID: %d, Child PID: %d\n", getpid(), pid);
    }

    return 0;
}
