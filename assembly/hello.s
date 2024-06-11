    .global main

    .text
main:
    mov message(%rip), %rdi
    call puts
    ret

message:
    .asciz "hello world\n"
