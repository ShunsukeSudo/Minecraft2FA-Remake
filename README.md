# Minecraft2FA

Provides 2FA feature in Minecraft server/proxy


## Setup

### Server

1. Download file from releases
2. Put .jar file into plugins folder
3. Run server/proxy
4. Shutdown server/proxy
5. Edit config.yml
6. Done

### Discord

1. Goto https://discord.com/developers/applications
2. Create bot
3. Copy token
4. Paste into config.yml


## Configuration

### Discord

```
discord:
  bot_token: 'TOKEN'
```

#### Bot token

Set discord token here


### Database

```
database:
  type: 'SQLITE'
  address: 'database.db'
  username: ''
  password: ''
```

#### Type

Supported types are

- SQlite
- MYSQL

#### address

When database type is SQLITE address is become filename. file is saved in plugins/mc2fa folder

#### Username

Only used for MYSQL

#### Password

Only used for MYSQL

### totp_auth

```
totp_auth:
  sessionExpireTimeInSeconds: 3600
  totpIssuerName: MC2FA
```

#### sessionExpireTimeInSeconds

When session is expired.

#### TOTP Issuer Name

Appeared in authenticator