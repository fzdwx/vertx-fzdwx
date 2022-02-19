# set shell := ["bash.exe","-c"]
alias bs := bootStartp

default:
    @just --list

# 构建chat lifecycle => [clean,compile,package,install...]
chat lifecycle:
     mvn -f ./chat-serv/pom.xml -DskipTests=true {{lifecycle}}

# 进行构建准备工作，会运行dep、common、bootStartp
pre: dep common bootStartp

# install depchain
dep:
    mvn -f ./depchain/pom.xml -DskipTests=true clean install
    @echo install [dep] success

# install common
common:
    mvn -f ./common/pom.xml -DskipTests=true clean install
    @echo install [common] success

# install bootStartp
bootStartp:
    mvn -f ./boot-startp/pom.xml -DskipTests=true clean install
    @echo install [serv] success

bk:
    ./gb.exe -c 100 -n 100 -d false -p request.curl.txt