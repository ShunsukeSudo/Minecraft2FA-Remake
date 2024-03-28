# Initial settings for testing

## Backend server

### Enabling BungeeCord mode

1. Open `backend-server` directory
2. Open `spigot.yml`
3. Edit `bungeecord: false` to `bungeecord: true`
```
settings:
  ~~ Some other settings ~~
  bungeecord: true
```
4. Done

## Proxy server

### Enabling ip forwarding and online mode

1. Open `proxy` directory
2. Open `config.yml`
3. Edit `ip_forward false` to `ip_forward true`
4. Edit `online_mode: false` to `online_mode: true` if disabled.
5. Done

### Defining backend-server

1. Open `proxy` directory
2. Open `config.yml`
3. Edit below
```yaml
servers:
  lobby:
    motd: '&1Just another Waterfall - Forced Host'
    address: localhost:25565
    restricted: false
```
4. To below
```yaml
servers:
  lobby:
    motd: '&1Just another Waterfall - Forced Host'
    address: proxy-backend-server:25565
    restricted: false
```

# Other information

## Server port

Proxy server expose 25565 port in outside of container.

Backend server expose 25560 port in outside of container.