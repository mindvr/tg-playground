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

`docker run -e TG_TOKEN={$TG_TOKEN} -e TG_NAME={$TG_NAME} tg-playground` 