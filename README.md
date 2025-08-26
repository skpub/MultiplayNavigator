# MultiplayNavigator

マイクラのSpigot, Paper, Purpurサーバ用の座標管理・ナビゲーションプラグインです。
コマンドリファレンスは以下の通り。

A coordinate management and navigation plugin for Spigot, Paper, and Purpur Minecraft servers.

## /beacon

座標を管理する。

Manage coordinates.

### /beacon add

座標を追加する。

Add a coordinate.

```
/beacon add (world|nether|the_end) name_of_coord x y z
```

Example
```
/beacon add world home 12 63 -30
/beacon add the_end nerest_city -1302 63 310
```

### /beacon delete

保存されている座標を削除する。

Delete a saved coordinate.

```
/beacon delete name_of_coord
```

### /beacon list

保存されている座標を一覧する。

List saved coordinates.

```
/beacon list
/beacon list (world|nether|the_end)
```

## /navigate

ナビゲーションを開始・終了する。

Start or end navigation.

### /navigate begin

ナビゲーションを開始する。

Start navigation.

```
/navigate begin name_of_coord
```

### /navigate end

ナビゲーションを終了する。

End navigation.

```
/navigate end
```