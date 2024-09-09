# Startcode for JPA projects

## How to use the EntityManagerFactory through HibernateConfig

It can run in two modes: normal and test. Normal is default and test is 
activated by setting a parameter HibernateConfig.TEST when creating the
EntityManagerFactory by calling the .

```java
EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig(state, dbName);
``` 

The state is either HibernateConfig.NORMAL or HibernateConfig.TEST and dbName is 
the name of the database.

If the state is HibernateConfig.TEST, then a test container is started and
the database is dropped and created.
