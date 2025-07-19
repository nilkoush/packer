# 👜 Packer
 - Plugin or App util to generate ResourcePack (packer.zip) with ItemModel (meta#setItemModel) or CustomModelDataComponent (meta#getCustomModelDataComponent) ready item models
 - Then the pack.zip can be import to for example [Nexo](https://docs.nexomc.com/)

Everything starts in yml files inside /items directory.
You can create as many .yml configurations, which can be separated to their own directories, as you want.

## ItemModel
If you want to generate .json model file for you texture (which you need to upload before) it can be done like this
```
items_example_item:
  texture: "example:texture"
```

If you want to generate .json model file for you texture with parent model
You need to upload the parent model, but the model from texture will be generated.
```
items_example_parent_item:
  parent_model: "example:parent_model"
  texture: "example:texture"
```

If you just want to only add the model and generate ItemModel from it
```
items_example_model:
  model: "example:model"
```

## CustomModelDataComponent
All of the above applies but you need to add 2 more properties for the configuration section and thats
```
cmd_example_item:
  model: "example:model" # this can be either model or texture
  material: "echo_shard"
  custom_model_data:
    strings: ["example_string"]
```

-----
### If you want to make those items dyable just add to any configuration section
```
items_dyeable_texture:
  texture: "example:texture"
  dyeable: true
```
-----

## Commands (if you use plugin on the server)
```/pack generate - Generates packer.zip from the configuration files in /items.```
```/pack items <category> - Opens menu with the specified category```
