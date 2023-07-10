# tg-playground
Java Telegram Bot demos

## How to run

### BotFather

Register bot at [BotFather](https://t.me/BotFather).

#### Command list (BotFather format)

```
help - send available command list
with-buttons - buttons in message and some interactivity
```

### Building

`docker build -t tg-playground .`

### Running

create `secrets.env` with contents

```env
TG_TOKEN=
TG_NAME=
```

run
```shell
docker compose up

#or
docker compose up -d
```