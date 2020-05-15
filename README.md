# Oakin
-- _Odin (is said to have broken a branch of Yggdrasil) + Oak._

## About
Spigot 1.15以降に対応した簡素な樵プラグイン.

<img src="https://raw.githubusercontent.com/blackbracken/Oakin/master/cap.gif" width="70%" height="70%">

## Config(ja)
```yaml
Enable:
  # デフォルトで有効にするか
  Default: true
  # スニーク中に有効にするか
  WhenSneaking: false
  # スニークしていないときに有効にするか
  WhenNotSneaking: true

# 1ブロック破壊するのに発生する遅延. 1/20秒単位で指定.
CuttingInterval: 2

# 樵が発動する道具を限定するか
LimitTools: true

# 道具のリスト; Minecraftのアイテムidに対応する
Cutters:
  - AIR
  - DIAMOND_AXE
  - GOLDEN_AXE
  - IRON_AXE
  - STONE_AXE
  - WOODEN_AXE

# 植えなおすか否か
ReplantSaplings: true

# 上級者向けの設定; 木を探索するときの再帰の深さのリミットを指定
RecursionLimit: 400
```

## Releases
ダウンロードは[releases](https://github.com/blackbracken/Oakin/releases)へ

## Commands

|コマンド|機能|
|:-:|:-:|
|`/orkin`|木こり機能のトグル|

## Permissions

|パーミッション|効果|
|:-:|:-:|
|`orkin.toggle`|トグルコマンドの利用|

## License
GPLv3 (viral license because of Bukkit)