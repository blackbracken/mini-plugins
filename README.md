# Oakin
-- Odin (is said to have broken a branch of Yggdrasil) + Oak.

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

ReplantSaplings: true
```