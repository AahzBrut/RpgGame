package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentHook
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import ru.aahzbrut.rpggame.ai.StateContext

class BehaviourTreeComponent(
    val nearbyEntities: MutableSet<Entity> = mutableSetOf(),
    val treePath: String = ""
) : Component<BehaviourTreeComponent> {
    companion object : ComponentType<BehaviourTreeComponent>() {
        private val treeParser = BehaviorTreeParser<StateContext>()

        val onBehaviourTreeAdd: ComponentHook<BehaviourTreeComponent> = { entity, component ->
            component.behaviourTree = treeParser.parse(Gdx.files.internal(component.treePath), StateContext(entity, this))
        }
    }

    lateinit var behaviourTree: BehaviorTree<StateContext>

    override fun type() = BehaviourTreeComponent
}
