package com.watch_dex.core.data

//sealed class PokemonType(
//    val identity: Type,
//    val dealsSuperEffective: List<Type>,
//    val takesSuperEffective: List<Type>,
//    val immuneTo: List<Type> = listOf()
//)
//
//object Bug : PokemonType(
//    Type.Bug,
//    listOf(Type.Grass, Type.Psychic, Type.Dark),
//    listOf(Type.Fire, Type.Flying, Type.Rock)
//)
//
//object Dark : PokemonType(
//    Type.Dark,
//    listOf(Type.Ghost, Type.Psychic),
//    listOf(Type.Fighting, Type.Bug, Type.Fairy),
//    listOf(Type.Psychic)
//)
//
//object Dragon : PokemonType(
//    Type.Dragon,
//    listOf(Type.Dragon),
//    listOf(Type.Ice, Type.Dragon, Type.Fairy)
//)
//
//object Electric : PokemonType(
//    Type.Electric,
//    listOf(Type.Water, Type.Flying),
//    listOf(Type.Ground)
//)
//
//object Fairy : PokemonType(
//    Type.Fairy,
//    listOf(Type.Fighting, Type.Dark, Type.Dragon),
//    listOf(Type.Poison, Type.Steel),
//    listOf(Type.Dragon)
//)
//
//object Fighting : PokemonType(
//    Type.Fighting,
//    listOf(
//        Type.Normal,
//        Type.Ice,
//        Type.Rock,
//        Type.Dark,
//        Type.Steel
//    ),
//    listOf(Type.Flying, Type.Psychic, Type.Fairy)
//)
//
//object Fire : PokemonType(
//    Type.Fire,
//    listOf(Type.Grass, Type.Ice, Type.Bug, Type.Steel),
//    listOf(Type.Water, Type.Ground, Type.Rock)
//)
//
//object Flying : PokemonType(
//    Type.Flying,
//    listOf(Type.Grass, Type.Fighting, Type.Bug),
//    listOf(Type.Electric, Type.Ice, Type.Rock),
//    listOf(Type.Ground)
//)
//
//object Ghost : PokemonType(
//    Type.Ghost,
//    listOf(Type.Psychic, Type.Ghost),
//    listOf(Type.Ghost, Type.Dark),
//    listOf(Type.Normal, Type.Fighting)
//)
//
//object Grass : PokemonType(
//    Type.Grass,
//    listOf(Type.Water, Type.Ground, Type.Rock),
//    listOf(
//        Type.Fire,
//        Type.Ice,
//        Type.Poison,
//        Type.Flying,
//        Type.Bug
//    )
//)
//
//object Ground : PokemonType(
//    Type.Ground,
//    listOf(
//        Type.Fire,
//        Type.Electric,
//        Type.Poison,
//        Type.Rock,
//        Type.Steel
//    ),
//    listOf(Type.Water, Type.Grass, Type.Ice),
//    listOf(Type.Electric)
//)
//
//object Ice : PokemonType(
//    Type.Ice,
//    listOf(Type.Grass, Type.Ground, Type.Flying, Type.Dragon),
//    listOf(Type.Fire, Type.Fighting, Type.Rock, Type.Steel)
//)
//
//object Normal : PokemonType(
//    Type.Normal,
//    listOf(),
//    listOf(Type.Fighting),
//    listOf(Type.Ghost)
//)
//
//object Poison : PokemonType(
//    Type.Poison,
//    listOf(Type.Grass, Type.Fairy),
//    listOf(Type.Ground, Type.Psychic)
//)
//
//object Psychic : PokemonType(
//    Type.Psychic,
//    listOf(Type.Fighting, Type.Poison),
//    listOf(Type.Bug, Type.Ghost, Type.Dark)
//)
//
//object Rock : PokemonType(
//    Type.Rock,
//    listOf(Type.Fire, Type.Ice, Type.Flying, Type.Bug),
//    listOf(
//        Type.Water,
//        Type.Grass,
//        Type.Fighting,
//        Type.Ground,
//        Type.Steel
//    )
//)
//
//object Steel : PokemonType(
//    Type.Steel,
//    listOf(Type.Ice, Type.Rock, Type.Fairy),
//    listOf(Type.Fire, Type.Fighting, Type.Ground),
//    listOf(Type.Poison)
//)
//
//object Water : PokemonType(
//    Type.Water,
//    listOf(Type.Fire, Type.Ground, Type.Rock),
//    listOf(Type.Electric, Type.Grass)
//)
