![banner](https://github.com/ImCodist/quick-menu/assets/50346006/9caa6fb8-4bbd-4aef-bb28-ceb161deeba6)
![Version](https://img.shields.io/github/v/release/tehbeard/quick-menu?style=flat-square) 
![Downloads](https://img.shields.io/github/downloads/tehbeard/quick-menu/total?style=flat-square)
![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/tehbeard/quick-menu/build.yml?style=flat-square)
![License](https://img.shields.io/github/license/tehbeard/quick-menu?style=flat-square)

# quick-menu
**Quick Menu** is a **client-side** mod for Minecraft that adds a **configurable easy-to-access menu** that runs **commands, keybinds and other actions**.<br>
This allows you to:
- Save a commonly used command such as `/home` to a dedicated button (with optional keybind)
- Configure more complex chains of commands with delays (e.g. `/lobby` , wait a second , `/skyblock`)
- Move uncommonly used keybinds for other mods to a menu to allow continued access to them, without cluttering limited keybinds.

The mod uses the [lib-gui](https://github.com/CottonMC/LibGui) GUI library (included in the jar) and [Fabric API](https://modrinth.com/mod/fabric-api).<br>

## Download
You can download the **latest version** in the [Releases](https://github.com/tehbeard/quick-menu/releases) of this repo.<br>
Older versions of the mod are also available to download on [Modrinth](https://modrinth.com/mod/quick-menu).

## Features
<img src="https://github.com/ImCodist/quick-menu/assets/50346006/e6ce7cf2-43f5-442e-bf90-8d912eb0fa58" alt="In-Game Preview"/>

- Easy to access menu with a **keybind** *(Default: G)*
- A simple action button **editor**.
- Each action button can run **as many actions** as you want.
- The **icon** for an action button can be selected with a **dedicated GUI**.

## How to Use
**Open the menu** in-game with the **chosen keybind** *(Default: G)*.<br>
To enter **edit mode** click the pencil icon next to the title or press E on the keyboard.<br>

In **edit mode** you can:
- Create a new action using the button below the main menu.
- Delete an action button by right-clicking the button you'd like to delete.

## TODO
- [x] Keybinds to activate each button.
- [ ] Group system that can show or hide itself when not active. (Server specific groups)
- [ ] More action types. (Keys, Menus, Timers)
- [ ] More customization options. (Different themes, change button size)
- [ ] Advanced action button options. (Should it close on pressed, run multiple times, toggle on and off)

---

License: [GPL v3](https://www.gnu.org/licenses/gpl-3.0.en.html)\
Version Format: Matched to game version (with `-n` suffix if necessary to denote bugfixes etc.)
