with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'r') as f:
    content = f.read()

# I see what I did: I accidentally replaced "VaultItem" with "VaultItemCard" inside the class parameters, but I also broke something else.
content = content.replace("VaultItemCard\n    item: VaultItem", "fun VaultItemCard(\n    item: VaultItem")
content = content.replace("Unresolved reference 'compose'.", "")
content = content.replace(".androidx.compose.ui.draw.shadow", ".shadow")

with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'w') as f:
    f.write(content)
