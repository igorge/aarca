package gie.aarca

trait LooserTrait { this: ArcanoidStage =>

    def impl_gameOver(){

        logger.info("GAME OVER!")

//        stageController.enqueue_replaceStage{ stageController =>
//            new ArcanoidStage(1, stageController)
//        }

        stageController.enqueue_popStage()

    }

}