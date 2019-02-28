# demo for showing [morphia](https://github.com/MorphiaOrg/morphia) issue#1314 

## way 1:
1. import project into intelliJ IDEA and run it . you will see that  
```
********************************** Mongo has mapped TestDO? Result :true
```


## way 2:
go to the root directory of the project, run with ```mvn clean package ```
and then run it ```java -jar target/morphia-test-executable.jar```
you will see that:
```
********************************** Mongo has mapped TestDO? Result :false
```
