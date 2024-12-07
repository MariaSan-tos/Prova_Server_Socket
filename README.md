### Comandos Disponíveis:

- **USER <username>**: Inicia a autenticação. Use:

```
USER user
```

- **PASS <password>**: Completa a autenticação. Use:

```
PASS pass
```

- **LIST**: Obtém a lista de arquivos. (Abra outra instancia telnet para receber os arquivos usando:
```bash
telnet 127.0.0.1 63929
```
ou a porta informada no console.)

- **QUIT**: Encerra a sessão.

## Observações

- O servidor foi configurado para operar na **porta 2121**.
- A autenticação é simples e usa **"user"** como nome de usuário e **"pass"** como senha. 
- O comando **LIST** usa uma porta de dados aleatória para a transferência da lista de arquivos.
