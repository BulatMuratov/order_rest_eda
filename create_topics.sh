#!/bin/bash

BOOTSTRAP_SERVER="kafka-1:29092"

create_topic() {
  docker exec kafka-1 kafka-topics --create \
    --bootstrap-server $BOOTSTRAP_SERVER \
    --replication-factor 3 \
    --partitions 3 \
    --topic $1
}

# 1. Жизненный цикл заказа (Создание заказа)
create_topic orders.lifecycle

# 2. Статусы инвентаризации (Успех/Провал резерва)
create_topic inventory.status

# 3. Команды для оплаты (Запуск процесса оплаты)
create_topic orders.commands

# 4. Статусы оплаты (Успех/Ошибка списания)
create_topic payments.lifecycle

# 5. Финальные команды инвентаризации (Подтверждение или Откат резерва)
create_topic inventory.commands