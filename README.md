# MultiplayNavigator

Spigot, Paper, Purpurサーバ用の座標管理・ナビゲーションプラグインです。
コマンドリファレンスは以下の通り。

## /beacon

座標を管理する

### /beacon add

座標を追加する

```
/beacon add (world|nether|end) name_of_coord x y z
```

sample
```
/beacon add world home 12 63 -30
/beacon add end nerest_city -1302 63 310
```

### /beacon delete

保存されている座標を削除する

```
/beacon delete name_of_coord
```

### /beacon list

保存されている座標を一覧する

```
/beacon list
/beacon list (world|nether|end)
```

## /navigate

ナビゲーションを開始・終了する

### /navigate begin

ナビゲーションを開始する

```
/navigate begin name_of_coord
```

### /navigate end

ナビゲーションを終了する

```
/navigate end
```