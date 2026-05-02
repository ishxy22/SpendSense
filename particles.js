// Setup Canvas for high-performance particles
const canvas = document.getElementById('particle-canvas');
if (canvas) {
    const ctx = canvas.getContext('2d');

    let width = canvas.width = window.innerWidth;
    let height = canvas.height = window.innerHeight;

    window.addEventListener('resize', () => {
        width = canvas.width = window.innerWidth;
        height = canvas.height = window.innerHeight;
    });

    const particles = [];
    let lastScrollY = window.scrollY || 0;
    let accumulatedScroll = 0;

    class Particle {
        constructor(x, y) {
            this.x = x;
            this.y = y;
            this.size = Math.random() * 150 + 80;
            this.speedY = Math.random() * 3 + 2;
            this.life = 1.0;
            this.decay = Math.random() * 0.015 + 0.008;
            this.isPink = Math.random() > 0.5;
        }

        update() {
            this.y -= this.speedY;
            this.life -= this.decay;
        }

        draw(ctx) {
            if (this.life <= 0) return;

            const gradient = ctx.createRadialGradient(
                this.x, this.y, 0,
                this.x, this.y, this.size / 2
            );

            if (this.isPink) {
                gradient.addColorStop(0, `rgba(133,31,104,${this.life * 0.7})`);
                gradient.addColorStop(1, 'rgba(66,15,52,0)');
            } else {
                gradient.addColorStop(0, `rgba(29,15,99,${this.life * 0.7})`);
                gradient.addColorStop(1, 'rgba(13,6,46,0)');
            }

            ctx.fillStyle = gradient;
            ctx.beginPath();
            ctx.arc(this.x, this.y, this.size / 2, 0, Math.PI * 2);
            ctx.fill();
        }
    }

    function animateParticles() {
        ctx.clearRect(0, 0, width, height);

        for (let i = particles.length - 1; i >= 0; i--) {
            particles[i].update();
            particles[i].draw(ctx);
            if (particles[i].life <= 0) {
                particles.splice(i, 1);
            }
        }

        requestAnimationFrame(animateParticles);
    }

    animateParticles();

    // In case there is no scroll, let's just spawn some randomly over time
    setInterval(() => {
        if (particles.length < 20) {
            particles.push(new Particle(Math.random() * width, height + 100));
        }
    }, 200);

    document.addEventListener('scroll', () => {
        const scrollY = window.scrollY;

        const delta = Math.abs(scrollY - lastScrollY);
        accumulatedScroll += delta;
        lastScrollY = scrollY;

        // Spawn canvas particles very efficiently
        while (accumulatedScroll > 15) {
            particles.push(new Particle(Math.random() * width, height + 100));
            accumulatedScroll -= 15;
        }

        const maxScroll = Math.max(1, document.body.scrollHeight - window.innerHeight);
        const scrollFraction = scrollY / maxScroll;

        const blobBlue = document.querySelector('.blob-blue');
        const blobPink = document.querySelector('.blob-pink');

        if (blobBlue && blobPink) {
            blobBlue.style.opacity = Math.min(0.2 + scrollFraction * 1.5, 1);
            blobPink.style.opacity = Math.min(0.2 + scrollFraction * 1.5, 1);

            const moveY = 150 - (scrollFraction * 250);
            blobBlue.style.transform = `translateY(${moveY}px)`;
            blobPink.style.transform = `translateY(${moveY}px)`;
        }
    });

    window.dispatchEvent(new Event('scroll'));
}
