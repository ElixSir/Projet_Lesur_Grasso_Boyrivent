document.addEventListener("DOMContentLoaded", function() {
    var chaineCanvas = document.getElementsByClassName("chaine")
    var cycleCanvas = document.getElementsByClassName("cycle")
    
    console.log(solution);
    let chaines = solution.chaines
    let cycles = solution.cycles
    
    printChaines(chaineCanvas,chaines)
    printCycles(cycleCanvas,cycles)

    function initCanvasWindowForChaine(eltHtml, nbPaire) {
        const PARTICIPANT_SIZE = Parmetres.PARTICIPANT_SIZE
        const CANVAS_PADDING = Parmetres.CANVAS_PADDING
        const WIDTH_BETWIN_PARTICIPANTS = Parmetres.WIDTH_BETWIN_PARTICIPANTS
        const HEIGHT_BETWIN_PARTICIPANTS = Parmetres.HEIGHT_BETWIN_PARTICIPANTS

        eltHtml.height = (PARTICIPANT_SIZE) * 2 + HEIGHT_BETWIN_PARTICIPANTS + CANVAS_PADDING * 2
        eltHtml.width = (PARTICIPANT_SIZE + WIDTH_BETWIN_PARTICIPANTS) * nbPaire - WIDTH_BETWIN_PARTICIPANTS + CANVAS_PADDING * 2
    }

    function initCanvasWindowForCycle(eltHtml, nbPaire) {
        const PARTICIPANT_SIZE = Parmetres.PARTICIPANT_SIZE
        const CANVAS_PADDING = Parmetres.CANVAS_PADDING
        const WIDTH_BETWIN_PARTICIPANTS = Parmetres.WIDTH_BETWIN_PARTICIPANTS
        const HEIGHT_BETWIN_PARTICIPANTS = Parmetres.HEIGHT_BETWIN_PARTICIPANTS
        
        if( nbPaire > 0 ) {
            let size = (PARTICIPANT_SIZE * 1.5) * 2 + PARTICIPANT_SIZE + CANVAS_PADDING * 2
            eltHtml.height = size
            eltHtml.width = size
        } else {
            eltHtml.height = (PARTICIPANT_SIZE) * 2 + HEIGHT_BETWIN_PARTICIPANTS + CANVAS_PADDING * 2
            eltHtml.width = (PARTICIPANT_SIZE + WIDTH_BETWIN_PARTICIPANTS) * nbPaire - WIDTH_BETWIN_PARTICIPANTS + CANVAS_PADDING * 2
        }
    }

    function printChaines(chaineCanvas,chaines) {
        for (let i = 0; i < chaines.length; i++) {
            const chaine = chaines[i];
            let ctxChaine = chaineCanvas[i].getContext("2d")
    
            initCanvasWindowForChaine(chaineCanvas[i], chaine.length)
    
            const c = new Chaine(chaine)
            c.draw(ctxChaine)
        }
    }

    function printCycles(cycleCanvas,cycles) {
        for (let i = 0; i < cycles.length; i++) {
            const cycle = cycles[i];
            let ctxCycle = cycleCanvas[i].getContext("2d")
    
            initCanvasWindowForCycle(cycleCanvas[i], cycle.length)
    
            const c = new Cycle(cycle)
            c.draw(ctxCycle)
        }
    }
});

class Parmetres {
    static PARTICIPANT_SIZE = 50
    static CANVAS_PADDING = 10
    static WIDTH_BETWIN_PARTICIPANTS = 30
    static HEIGHT_BETWIN_PARTICIPANTS = 10
    static LINEWIDTH_PARTICIPANTS = 4
}

class Participant {
    w = Parmetres.PARTICIPANT_SIZE
    lineWidth = Parmetres.LINEWIDTH_PARTICIPANTS

    constructor(id) {
        this.id = id
    }

    drawArrow(context, fromx, fromy, tox, toy ) {
        var headlen = 10; // length of head in pixels
        var dx = tox - fromx;
        var dy = toy - fromy;
        var angle = Math.atan2(dy, dx);
        let color = 'rgb(20, 20, 20)'

        context.strokeStyle  = color

        context.beginPath()
        context.moveTo(fromx, fromy);
        context.lineTo(tox, toy);
        context.lineTo(tox - headlen * Math.cos(angle - Math.PI / 6), toy - headlen * Math.sin(angle - Math.PI / 6));
        context.moveTo(tox, toy);
        context.lineTo(tox - headlen * Math.cos(angle + Math.PI / 6), toy - headlen * Math.sin(angle + Math.PI / 6));
        context.stroke();
    }
}

class Paire extends Participant {

    color = 'rgb(0, 0, 200)'

    constructor(id) {
        super(id)
    }

    drawInCycle(ctx, pos, tailleCycle) {
        switch (tailleCycle) {
            /*
            case 2:
                this.#drawCycle2(ctx,pos)
                break;
            
            case 3:
                this.#drawCycle3(ctx,pos)
                break;
            */
            
            default:
                this.#drawCycleDefault(ctx,pos,tailleCycle)
                break;
        }
    }

    #drawCycleDefault(ctx, pos, tailleCycle) {
        this.#drawCycleInCircle(ctx,pos,tailleCycle)
    }    
    #drawCycleInCircle(ctx, pos, tailleCycle) {
        let a = pos * Math.PI / (tailleCycle/2) - Math.PI / 2,
            r = Parmetres.PARTICIPANT_SIZE * 1.5 ,
            x = Math.cos(a) * r + r + Parmetres.CANVAS_PADDING,
            y = Math.sin(a) * r + r + Parmetres.CANVAS_PADDING
        
        this.#draw(ctx,x,y)
        this.#drawArrowInCircle(ctx,pos, tailleCycle)
    }
    #drawArrowInCircle(ctx,pos, tailleCycle) {
        let a = pos * Math.PI / (tailleCycle/2) - Math.PI / 2,
            rp = this.w / 2,
            aPrec = (pos - 1) * Math.PI / (tailleCycle/2) - Math.PI / 2,
            r = Parmetres.PARTICIPANT_SIZE * 1.5 ,
            x = Math.cos(a) * r + r + Parmetres.CANVAS_PADDING,
            xPrec = Math.cos(aPrec) * r + r + Parmetres.CANVAS_PADDING,
            y = Math.sin(a) * r + r + Parmetres.CANVAS_PADDING,
            yPrec = Math.sin(aPrec) * r + r + Parmetres.CANVAS_PADDING,
            ap = 2* pos * Math.PI / (tailleCycle) + Math.PI - Math.PI / (tailleCycle),
            x1 = x + rp + Math.cos(ap) * rp,
            y1 = y + rp + Math.sin(ap) * rp,
            x2 = xPrec + rp + Math.cos(ap + Math.PI) * rp,
            y2 = yPrec + rp + Math.sin(ap + Math.PI) * rp
        
        this.drawArrow(ctx, x1 , y1 ,x2, y2 )
    }
    
    #drawCycle2(ctx, pos) { 
        this.drawInChaine(ctx,pos,1)
        if( pos == 1) {
            let w = this.w,
                offset = Parmetres.WIDTH_BETWIN_PARTICIPANTS,
                r = w/2,
                x = ((w + offset) + Parmetres.CANVAS_PADDING),
                xPrec = (Parmetres.CANVAS_PADDING),
                y = w + Parmetres.HEIGHT_BETWIN_PARTICIPANTS + Parmetres.CANVAS_PADDING,
                yPrec = (Parmetres.CANVAS_PADDING),
                a = -5* Math.PI / 6,
                x1 = x + r + Math.cos(a) * r,
                y1 = y + r + Math.sin(a) * r,
                x2 = xPrec + r + Math.cos(a + Math.PI) * r,
                y2 = yPrec + r + Math.sin(a + Math.PI) * r

            this.drawArrow(ctx,x1,y1,x2,y2);
        }
    }

    #drawCycle3(ctx, pos) {
        this.drawInChaine(ctx,pos,3)
        if( pos == 2) {
            let w = this.w,
                offset = Parmetres.WIDTH_BETWIN_PARTICIPANTS,
                r = w/2,
                x = (2 * (w + offset) + Parmetres.CANVAS_PADDING),
                xPrec = (Parmetres.CANVAS_PADDING),
                y = (Parmetres.CANVAS_PADDING),
                yPrec = (Parmetres.CANVAS_PADDING),
                a = Math.PI,
                x1 = x + r + Math.cos(a) * r,
                y1 = y + r + Math.sin(a) * r,
                x2 = xPrec + r + Math.cos(a + Math.PI) * r,
                y2 = yPrec + r + Math.sin(a + Math.PI) * r

            this.drawArrow(ctx,x1,y1,x2,y2);
        }
    }

    drawInChaine(ctx,pos) {
        let offset = Parmetres.WIDTH_BETWIN_PARTICIPANTS,
            w = this.w,
            x = ((pos)*(w + offset) + Parmetres.CANVAS_PADDING),
            xPrec = ((pos - 1)*(w + offset) + Parmetres.CANVAS_PADDING),
            y = ( (pos % 2)* (w + Parmetres.HEIGHT_BETWIN_PARTICIPANTS) + Parmetres.CANVAS_PADDING),
            yPrec = ( ((pos - 1) % 2)* (w + Parmetres.HEIGHT_BETWIN_PARTICIPANTS) + Parmetres.CANVAS_PADDING),
            a,
            r = w/2,
            x1line,
            y1line,
            x2line,
            y2line
        
        if( pos % 2 == 0 ) {
            a = 5 * Math.PI / 6
        } else {
            a = 7 * Math.PI / 6
        }

        x1line = xPrec + r + Math.cos(a + Math.PI) * r
        y1line = yPrec + r + Math.sin(a + Math.PI) * r

        x2line = x + r + Math.cos(a) * r
        y2line = y + r + Math.sin(a) * r

        this.#draw(ctx,x,y)
        if( pos != 0 ) {
            this.drawArrow(ctx, x1line , y1line ,x2line, y2line )
        }
    }

    #draw(ctx,x,y) {

        let lineWidth = this.lineWidth,
            color = this.color,
            w = this.w,
            h = this.w

        ctx.strokeStyle  = color
        ctx.lineWidth = lineWidth

        // Dessin du texte
        ctx.font = '20px serif'
        ctx.textBaseline = 'center'
        ctx.fillStyle = color
        let textMetrics = ctx.measureText(this.id)
        let fontHeight = textMetrics.actualBoundingBoxDescent - textMetrics.actualBoundingBoxAscent
        ctx.fillText(this.id, x + ( w - textMetrics.width) /2, y + (h-fontHeight) / 2)

        // Dessin du losange
        let r = w/2
        ctx.beginPath()
        // ctxChaine.moveTo(x + w/2,   y)
        ctx.arc(x + r,y + r,r - lineWidth/2,0,Math.PI * 2,true)
        
        ctx.stroke()
        ctx.closePath()
    }
}

class Altruiste extends Participant {
    
    color = 'rgb(200, 0, 0)'
    lineJoin = "round" // "bevel" || "round" || "miter"

    constructor(id) {
        super(id)
    }

    drawInChaine(ctx,pos) {
        let offset = Parmetres.CANVAS_PADDING,
            w = this.w,
            x = (pos*w + offset),
            y = ( (pos % 2)* (w + Parmetres.HEIGHT_BETWIN_PARTICIPANTS) + offset)

        this.#draw(ctx,x,y)
    }

    #draw(ctxChaine,x,y) {
        let color = this.color,
            id = this.id,
            w = this.w,
            h = this.w

        ctxChaine.strokeStyle  = color
        ctxChaine.lineWidth = this.lineWidth
        ctxChaine.lineJoin = this.lineJoin

        // Dessin du texte
        ctxChaine.font = '20px serif'
        ctxChaine.textBaseline = 'center'
        ctxChaine.fillStyle = color
        var textMetrics = ctxChaine.measureText(id)
        let fontHeight = textMetrics.actualBoundingBoxDescent - textMetrics.actualBoundingBoxAscent
        ctxChaine.fillText(id, x + ( w - textMetrics.width) /2, y + (h-fontHeight) / 2)

        // Dessin du losange
        ctxChaine.beginPath()
        ctxChaine.moveTo(x + w/2,   y)
        ctxChaine.lineTo(x + w,     y + h/2)
        ctxChaine.lineTo(x + w/2,   y + h)
        ctxChaine.lineTo(x + 0,     y + h/2)
        ctxChaine.closePath()
        ctxChaine.stroke()
    }

}

class Chaine {
    constructor(chaine) {
        this.array = chaine
    }

    draw(ctx) {
        const a = new Altruiste(this.array[0])

        a.drawInChaine(ctx,0)

        for (let i = 1; i < this.array.length; i++) {
            const id = this.array[i]
            const p = new Paire(id)

            p.drawInChaine(ctx,i)
        }
    }
}

class Cycle {
    constructor(cycle) {
        this.array = cycle
    }

    draw(ctx) {
        let i;
        for (i = 0; i < this.array.length; i++) {
            const id = this.array[i]
            const p = new Paire(id)

            p.drawInCycle(ctx,i,this.array.length)
        }
    }
}