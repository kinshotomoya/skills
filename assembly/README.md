

```shell

$ docker run -it --rm --platform linux/x86_64 -v ./hello.s:/hello.s --name debian gcc:11-bullseye
# gcc hello.s

root@be72fa252c1b:/# readelf --syms a.out

Symbol table '.dynsym' contains 4 entries:
   Num:    Value          Size Type    Bind   Vis      Ndx Name
     0: 0000000000000000     0 NOTYPE  LOCAL  DEFAULT  UND
     1: 0000000000000000     0 FUNC    GLOBAL DEFAULT  UND puts@GLIBC_2.2.5 (2)
     2: 0000000000000000     0 FUNC    GLOBAL DEFAULT  UND [...]@GLIBC_2.2.5 (2)
     3: 0000000000000000     0 NOTYPE  WEAK   DEFAULT  UND __gmon_start__

Symbol table '.symtab' contains 62 entries:
   Num:    Value          Size Type    Bind   Vis      Ndx Name
     0: 0000000000000000     0 NOTYPE  LOCAL  DEFAULT  UND
     1: 00000000004002a8     0 SECTION LOCAL  DEFAULT    1
     2: 00000000004002c4     0 SECTION LOCAL  DEFAULT    2
     3: 00000000004002e8     0 SECTION LOCAL  DEFAULT    3
     4: 0000000000400310     0 SECTION LOCAL  DEFAULT    4
     5: 0000000000400330     0 SECTION LOCAL  DEFAULT    5
     6: 0000000000400390     0 SECTION LOCAL  DEFAULT    6
     7: 00000000004003ce     0 SECTION LOCAL  DEFAULT    7
     8: 00000000004003d8     0 SECTION LOCAL  DEFAULT    8
     9: 00000000004003f8     0 SECTION LOCAL  DEFAULT    9
    10: 0000000000400428     0 SECTION LOCAL  DEFAULT   10
    11: 0000000000401000     0 SECTION LOCAL  DEFAULT   11
    12: 0000000000401020     0 SECTION LOCAL  DEFAULT   12
    13: 0000000000401040     0 SECTION LOCAL  DEFAULT   13
    14: 00000000004011a4     0 SECTION LOCAL  DEFAULT   14
    15: 0000000000402000     0 SECTION LOCAL  DEFAULT   15
    16: 0000000000402004     0 SECTION LOCAL  DEFAULT   16
    17: 0000000000402038     0 SECTION LOCAL  DEFAULT   17
    18: 0000000000403e00     0 SECTION LOCAL  DEFAULT   18
    19: 0000000000403e08     0 SECTION LOCAL  DEFAULT   19
    20: 0000000000403e10     0 SECTION LOCAL  DEFAULT   20
    21: 0000000000403ff0     0 SECTION LOCAL  DEFAULT   21
    22: 0000000000404000     0 SECTION LOCAL  DEFAULT   22
    23: 0000000000404020     0 SECTION LOCAL  DEFAULT   23
    24: 0000000000404030     0 SECTION LOCAL  DEFAULT   24
    25: 0000000000000000     0 SECTION LOCAL  DEFAULT   25
    26: 0000000000000000     0 FILE    LOCAL  DEFAULT  ABS crtstuff.c
    27: 0000000000401080     0 FUNC    LOCAL  DEFAULT   13 deregister_tm_clones
    28: 00000000004010b0     0 FUNC    LOCAL  DEFAULT   13 register_tm_clones
    29: 00000000004010f0     0 FUNC    LOCAL  DEFAULT   13 __do_global_dtors_aux
    30: 0000000000404030     1 OBJECT  LOCAL  DEFAULT   24 completed.0
    31: 0000000000403e08     0 OBJECT  LOCAL  DEFAULT   19 __do_global_dtor[...]
    32: 0000000000401120     0 FUNC    LOCAL  DEFAULT   13 frame_dummy
    33: 0000000000403e00     0 OBJECT  LOCAL  DEFAULT   18 __frame_dummy_in[...]
    34: 0000000000000000     0 FILE    LOCAL  DEFAULT  ABS /tmp/ccNihmDJ.o
    35: 0000000000401133     0 NOTYPE  LOCAL  DEFAULT   13 message
    36: 0000000000000000     0 FILE    LOCAL  DEFAULT  ABS crtstuff.c
    37: 0000000000402114     0 OBJECT  LOCAL  DEFAULT   17 __FRAME_END__
    38: 0000000000000000     0 FILE    LOCAL  DEFAULT  ABS
    39: 0000000000403e08     0 NOTYPE  LOCAL  DEFAULT   18 __init_array_end
    40: 0000000000403e10     0 OBJECT  LOCAL  DEFAULT   20 _DYNAMIC
    41: 0000000000403e00     0 NOTYPE  LOCAL  DEFAULT   18 __init_array_start
    42: 0000000000402004     0 NOTYPE  LOCAL  DEFAULT   16 __GNU_EH_FRAME_HDR
    43: 0000000000404000     0 OBJECT  LOCAL  DEFAULT   22 _GLOBAL_OFFSET_TABLE_
    44: 00000000004011a0     1 FUNC    GLOBAL DEFAULT   13 __libc_csu_fini
    45: 0000000000404020     0 NOTYPE  WEAK   DEFAULT   23 data_start
    46: 0000000000000000     0 FUNC    GLOBAL DEFAULT  UND puts@GLIBC_2.2.5
    47: 0000000000404030     0 NOTYPE  GLOBAL DEFAULT   23 _edata
    48: 00000000004011a4     0 FUNC    GLOBAL HIDDEN    14 _fini
    49: 0000000000000000     0 FUNC    GLOBAL DEFAULT  UND __libc_start_mai[...]
    50: 0000000000404020     0 NOTYPE  GLOBAL DEFAULT   23 __data_start
    51: 0000000000000000     0 NOTYPE  WEAK   DEFAULT  UND __gmon_start__
    52: 0000000000404028     0 OBJECT  GLOBAL HIDDEN    23 __dso_handle
    53: 0000000000402000     4 OBJECT  GLOBAL DEFAULT   15 _IO_stdin_used
    54: 0000000000401140    93 FUNC    GLOBAL DEFAULT   13 __libc_csu_init
    55: 0000000000404038     0 NOTYPE  GLOBAL DEFAULT   24 _end
    56: 0000000000401070     1 FUNC    GLOBAL HIDDEN    13 _dl_relocate_sta[...]
    57: 0000000000401040    43 FUNC    GLOBAL DEFAULT   13 _start
    58: 0000000000404030     0 NOTYPE  GLOBAL DEFAULT   24 __bss_start
    59: 0000000000401126     0 NOTYPE  GLOBAL DEFAULT   13 main
    60: 0000000000404030     0 OBJECT  GLOBAL HIDDEN    23 __TMC_END__
    61: 0000000000401000     0 FUNC    GLOBAL HIDDEN    11 _init
```


_startというラベルは最初に実行されるメモリアドレスを指している

プログラム実行時の流れ
- gccなどのように実行ファイルを作成する
  - この時にリンカーが非常に重要な役割
    - メモリ配置
      - プログラムの各部分がメモリ上のどこに配置されるかを決める
    - シンボルの解決
      - シンボルとは、プログラム中で関数や変数を一意に識別する名前のこと
      - コンパイラがコンパイルすると各関数、変数に対応するシンボルが生成される
- 実行ファイルがシェルから起動される
- OSがその実行ファイルの内容に従ってメモリ上にその実行ファイルに記載された内容を配置する
  - この命令はこのメモリアドレスに配置。。など
- メモリへのロードが終わると、CPUが特定のアドレス（_startとラベリングされた）から命令を読み込んで実行する
